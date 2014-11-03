package com.bstek.designer.common.surface;

import com.bstek.designer.common.model.DoradoProperyParser;
import com.bstek.designer.common.model.IDoradoModelParser;
import com.bstek.designer.core.DoradoDesignerEditor;
import com.bstek.designer.core.model.DoradoRadViewComponent;
import com.bstek.designer.core.palette.DoradoPaletteGroup;
import com.bstek.designer.core.rendering.DoradoRenderResult;
import com.bstek.designer.core.surface.DoradoDesignerEditorPanel;
import com.bstek.designer.core.surface.DoradoExternalPSIChangeListener;
import com.bstek.designer.core.surface.DoradoRootView;
import com.intellij.designer.componentTree.TreeComponentDecorator;
import com.intellij.designer.model.RadComponent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.xml.XmlFile;
import com.intellij.util.Alarm;
import com.intellij.util.ThrowableConsumer;
import com.intellij.util.ui.update.MergingUpdateQueue;
import com.intellij.util.xml.DomFileDescription;
import com.intellij.util.xml.impl.DomManagerImpl;
import com.sun.istack.internal.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

/**
 * 主编辑器设计面板
 * Created by robin on 13-12-5.
 */
public abstract class DoradoCommonDesignerEditorPanel extends DoradoDesignerEditorPanel {

    public XmlFile xmlFile;

    public DoradoExternalPSIChangeListener externalPSIChangeListener;

    public TreeComponentDecorator treeComponentDecorator;

    protected RadComponent radComponent;

    public boolean isActive;

    //protected EditableArea mySurfaceArea;

    protected Alarm sessionAlarm = new Alarm();

    public MergingUpdateQueue sessionQueue;

    //protected Project project;

    protected DoradoRootView doradoRootView;

    //private volatile RenderSession mySession;

    public DoradoCommonDesignerEditorPanel(@NotNull DoradoDesignerEditor editor,
                                           @NotNull Project project,
                                           @NotNull Module module,
                                           @NotNull final VirtualFile file) {
        super(editor, project, module, file);

//        showProgress("Developing and Coming Soon...");
//        treeComponentDecorator = getTreeDecorator();
//        sessionQueue = getDoradoMetaManager().getSessionQueue();
        sessionQueue = new MergingUpdateQueue("dorado.designer", 10, true, null, editor, null, Alarm.ThreadToUse.OWN_THREAD);
        xmlFile = (XmlFile) ApplicationManager.getApplication().runReadAction(new Computable<PsiFile>() {
            @Override
            @Nullable
            public PsiFile compute() {
                return PsiManager.getInstance(getProject()).findFile(file);
            }
        });
        //--PSI:Program Structure Interface
        externalPSIChangeListener = new DoradoExternalPSIChangeListener(this, xmlFile, 100, new Runnable() {
            @Override
            public void run() {
                reparseFile();
            }
        });
        externalPSIChangeListener.setInitialize();
        externalPSIChangeListener.activate();
        //--向alarm中增加请求
        externalPSIChangeListener.addRequest();

    }


    @Override
    public String getEditorText() {
        return ApplicationManager.getApplication().runReadAction(new Computable<String>() {
            @Override
            public String compute() {
                return xmlFile.getText();
            }
        });
    }

    @Override
    public void activate() {
        super.activate();
        updateRenderer(true);
    }

    private void updateRenderer(final boolean updateProperties) {
        if (getMyRootComponent() == null) {
            reparseFile();
            return;
        }
    }

    private void reparseFile() {
        try {
            //storeState();
            //showDesignerCard();

            parseFile(new Runnable() {
                @Override
                public void run() {
                    //showDesignerCard();
                    //myLayeredPane.revalidate();
                    restoreState();
                }
            });
        } catch (RuntimeException e) {
            //externalPSIChangeListener.clear();
            //showError("Parsing error", e.getCause() == null ? e : e.getCause());
        }
    }

    private void parseFile(final Runnable runnable) {
        //--文件转模型解析器生成
        final IDoradoModelParser parser = getModelParser(getProject(), xmlFile);

        createRenderer(new Throwable(), new ThrowableConsumer<DoradoRenderResult, Throwable>() {
            @Override
            public void consume(DoradoRenderResult result) throws Throwable {
//                RenderSession session = result.getSession();
//                if (session == null) {
//                    return;
//                }
//
//                if (!session.getResult().isSuccess()) {
//                    // This image may not have been fully rendered before some error caused
//                    // the render to abort, but a partial render is better. However, if the render
//                    // was due to some configuration change, we don't want to replace the image
//                    // since all the mouse regions and model setup will no longer match the pixels.
//                    if (myRootView != null && myRootView.getImage() != null && session.getImage() != null &&
//                            session.getImage().getWidth() == myRootView.getImage().getWidth() &&
//                            session.getImage().getHeight() == myRootView.getImage().getHeight()) {
//                        myRootView.setImage(session.getImage(), session.isAlphaChannelImage());
//                        myRootView.repaint();
//                    }
//                    return;
//                }
//
//                boolean insertPanel = !myShowingRoot;
                if (doradoRootView == null) {
                    doradoRootView = new DoradoRootView(DoradoCommonDesignerEditorPanel.this);
                }


//                if (myRootView == null) {
//                    myRootView = new RootView(AndroidDesignerEditorPanel.this, 0, 0, session.getImage(), session.isAlphaChannelImage());
//                    myRootView.addComponentListener(new ComponentAdapter() {
//                        @Override
//                        public void componentResized(ComponentEvent componentEvent) {
//                            zoomToFitIfNecessary();
//                        }
//                    });
//                    insertPanel = true;
//                }
//                else {
//                    myRootView.setImage(session.getImage(), session.isAlphaChannelImage());
//                    myRootView.updateBounds(true);
//                }
//                try {
//                    parser.updateRootComponent(myConfiguration.getFullConfig(), session, myRootView);
//                }
//                catch (Throwable e) {
//                    myRootComponent = parser.getRootComponent();
//                    throw e;
//                }
                //--生成idea中需要的RadViewComponent模型
                DoradoRadViewComponent newRootComponent = parser.getRootComponent();
//                newRootComponent.setClientProperty(DoradoModelParser.XML_FILE_KEY, myXmlFile);
//                newRootComponent.setClientProperty(DoradoModelParser.MODULE_KEY, AndroidDesignerEditorPanel.this);
//                newRootComponent.setClientProperty(TreeComponentDecorator.KEY, myTreeDecorator);
//
//                IAndroidTarget target = myConfiguration.getTarget();
//                assert target != null; // otherwise, rendering would not have succeeded
                //--对象属性解析器生成
                DoradoProperyParser propertyParser = new DoradoProperyParser();
                propertyParser.loadRecursive(newRootComponent);
//                newRootComponent.setClientProperty(PropertyParser.KEY, propertyParser);
//
                boolean firstRender = getMyRootComponent() == null;
//
                setMyRootComponent(newRootComponent);

//                if (firstRender) {
//                    //--寻找View节点
//                    List list = Collections.emptyList();
//                    List<RadComponent> children = newRootComponent.getChildren();
//                    for (RadComponent radComponent : children) {
//                        if (radComponent instanceof DoradoRadViewComponent) {
//                            String tagName = ((DoradoRadViewComponent) radComponent).getTag().getLocalName();
//                            if (tagName.equals("View")) {
//                                list = Collections.<RadComponent>singletonList(radComponent);
//                                break;
//                            }
//                        }
//                    }
//                    if (list.isEmpty()) {
//                        list = Collections.<RadComponent>singletonList(newRootComponent);
//                    }
                //getMySurfaceArea().setSelection(list);
//                    }
                //}
//
//                if (insertPanel) {
//                    // Use a custom layout manager which adjusts the margins/padding around the designer canvas
//                    // dynamically; it will try to use DEFAULT_HORIZONTAL_MARGIN * DEFAULT_VERTICAL_MARGIN, but
//                    // if there is not enough room, it will split the margins evenly in each dimension until
//                    // there is no room available without scrollbars.
//                    JPanel rootPanel = new JPanel(new LayoutManager() {
//                        @Override
//                        public void addLayoutComponent(String s, Component component) {
//                        }
//
//                        @Override
//                        public void removeLayoutComponent(Component component) {
//                        }
//
//                        @Override
//                        public Dimension preferredLayoutSize(Container container) {
//                            return new Dimension(0, 0);
//                        }
//
//                        @Override
//                        public Dimension minimumLayoutSize(Container container) {
//                            return new Dimension(0, 0);
//                        }
//
//                        @Override
//                        public void layoutContainer(Container container) {
//                            myRootView.updateBounds(false);
//                            int x = Math.max(2, Math.min(DEFAULT_HORIZONTAL_MARGIN, (container.getWidth() - myRootView.getWidth()) / 2));
//                            int y = Math.max(2, Math.min(DEFAULT_VERTICAL_MARGIN, (container.getHeight() - myRootView.getHeight()) / 2));
//
//                            // If we're squeezing the image to fit, and there's a drop shadow showing
//                            // shift *some* space away from the tail portion of the drop shadow over to
//                            // the left to make the image look more balanced
//                            if (myRootView.getShowDropShadow()) {
//                                if (x <= 2) {
//                                    x += ShadowPainter.SHADOW_SIZE / 3;
//                                }
//                                if (y <= 2) {
//                                    y += ShadowPainter.SHADOW_SIZE / 3;
//                                }
//                            }
//
//                            if (myMaxWidth > 0) {
//                                myRootView.setLocation(Math.max(0, (myMaxWidth - myRootView.getScaledWidth()) / 2),
//                                        2 + Math.max(0, (myMaxHeight - myRootView.getScaledHeight()) / 2));
//                            } else {
//                                myRootView.setLocation(x, y);
//                            }
//                        }
//                    });
//
//                    rootPanel.setBackground(AndroidLayoutPreviewPanel.DESIGNER_BACKGROUND_COLOR);
//                    rootPanel.setOpaque(true);
//                    rootPanel.add(myRootView);
                //JPanel rootPanel = new JPanel();
                //myLayeredPane.add(rootPanel, LAYER_COMPONENT);
//                    myShowingRoot = true;
//                }
//                zoomToFitIfNecessary();
//
                //loadInspections(new EmptyProgressIndicator());
//                updateInspections();
//
//                if (RenderPreviewMode.getCurrent() != RenderPreviewMode.NONE) {
//                    RenderPreviewManager previewManager = getPreviewManager(true);
//                    if (previewManager != null) {
//                        previewManager.renderPreviews();
//                    }
//                }
//
                runnable.run();
            }
        });
    }


    protected abstract IDoradoModelParser getModelParser(Project project, XmlFile xmlFile);

    //
//    //--销毁渲染器
//    private void disposeRenderer() {
////        if (mySession != null) {
////            mySession.dispose();
////            mySession = null;
////        }
//    }
//
//
//    //--创建渲染器
    private void createRenderer(final Throwable throwable,
                                final ThrowableConsumer<DoradoRenderResult, Throwable> runnable) {
        //disposeRenderer();
//        if (myConfiguration == null) {
//            return;
//        }
        System.out.println(runnable);

//        sessionAlarm.addRequest(new Runnable() {
//            @Override
//            public void run() {
        //if (mySession == null) {
        //showProgress("Initializing Rendering Library...");
        //}
//            }
//        }, 500);

//        final long sessionId = ++mySessionId;

        sessionQueue.queue(new com.intellij.util.ui.update.Update("doradoRender") {
            private void cancel() {
                sessionAlarm.cancelAllRequests();
                ApplicationManager.getApplication().invokeLater(new Runnable() {
                    @Override
                    public void run() {
//                        if (!isProjectClosed()) {
//                            hideProgress();
//                        }
                    }
                });
            }


            public void run() {
                ApplicationManager.getApplication().invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        //RenderResult renderResult = RenderResult.createBlank(myXmlFile, logger);
                        try {
                            //hideProgress();
                            runnable.consume(null);
                        } catch (final Throwable e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    public List<DoradoPaletteGroup> getPaletteGroups() {
        return Collections.emptyList();
    }

    public DomFileDescription getDomFileDescription() {
        PsiFile psiFile = PsiManager.getInstance(getProject()).findFile(getFile());
        if (psiFile instanceof XmlFile) {
            DomFileDescription domFileDescription = DomManagerImpl.getDomManager(getProject()).getDomFileDescription(((XmlFile) psiFile));
            return domFileDescription;
        }
        return null;
    }
//
//    @Override
//    public boolean execute(ThrowableRunnable<Exception> operation, final boolean updateProperties) {
//        if (!ReadonlyStatusHandler.ensureFilesWritable(getProject(), myFile)) {
//            return false;
//        }
//        try {
//            externalPSIChangeListener.stop();
//            operation.run();
//            ApplicationManager.getApplication().invokeLater(new Runnable() {
//                @Override
//                public void run() {
//                    boolean active = externalPSIChangeListener.isActive();
//                    if (active) {
//                        externalPSIChangeListener.stop();
//                    }
//                    updateRenderer(updateProperties);
//                    if (active) {
//                        externalPSIChangeListener.start();
//                    }
//                }
//            });
//            return true;
//        } catch (Throwable e) {
//            showError("Execute command", e);
//            return false;
//        } finally {
//            externalPSIChangeListener.start();
//        }
//    }

}