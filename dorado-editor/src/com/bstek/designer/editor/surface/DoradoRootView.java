package com.bstek.designer.editor.surface;

import com.intellij.android.designer.designSurface.TransformedComponent;
import com.intellij.android.designer.designSurface.graphics.DesignerGraphics;
import com.intellij.android.designer.designSurface.graphics.DrawingStyle;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by robin on 14-5-21.
 */
public class DoradoRootView
        //extends JComponent implements TransformedComponent
        {
//    private List<EmptyRegion> myEmptyRegions;
//    private final ViewDesignerEditorPanel panel;
//    protected int myX;
//    protected int myY;
//    protected ScalableImage myScalableImage;
//
//    public DoradoRootView(ViewDesignerEditorPanel panel, int x, int y, RenderResult renderResult) {
//        myX = x;
//        myY = y;
//        this.panel = panel;
//        myScalableImage = renderResult.getImage();
//    }
//
//    private DoradoRootView(ViewDesignerEditorPanel panel) {
//        this.panel = panel;
//    }
//
//    public ViewDesignerEditorPanel getPanel() {
//        return panel;
//    }
//
//    public BufferedImage getImage() {
//        return myScalableImage != null ? myScalableImage.getOriginalImage() : null;
//    }
//
//    public ScalableImage getScalableImage() {
//        return myScalableImage;
//    }
//
//    public void setRenderedImage(ScalableImage image) {
//        myEmptyRegions = null;
//        myScalableImage = image;
//        updateBounds(true);
//        repaint();
//    }
//
//    public boolean getShowDropShadow() {
//        if (myScalableImage != null) {
//            return myScalableImage.getShowDropShadow();
//        } else {
//            return false;
//        }
//    }
//
//    @Override
//    public void paintComponent(Graphics g) {
//        super.paintComponent(g);
//        paintImage(g);
//    }
//
//    public void updateSize() {
//        updateBounds(true);
//    }
//
//    protected void updateBounds(boolean imageChanged) {
//        if (myScalableImage == null) {
//            return;
//        }
//        if (panel.isZoomToFit()) {
//            panel.zoomToFitIfNecessary();
//        }
//
//        double zoom = panel.getZoom();
//        myScalableImage.setScale(zoom);
//        Dimension requiredSize = myScalableImage.getRequiredSize();
//        int newWidth = requiredSize.width;
//        int newHeight = requiredSize.height;
//        if (getWidth() != newWidth || getHeight() != newHeight) {
//            setSize(newWidth, newHeight);
//            myScalableImage.imageChanged();
//        } else if (imageChanged) {
//            myScalableImage.imageChanged();
//        }
//    }
//
//    public void addEmptyRegion(int x, int y, int width, int height) {
//        if (myScalableImage == null) {
//            return;
//        }
//        BufferedImage image = myScalableImage.getOriginalImage();
//        if (new Rectangle(0, 0, image.getWidth(), image.getHeight()).contains(x, y)) {
//            EmptyRegion r = new EmptyRegion();
//            r.myX = x;
//            r.myY = y;
//            r.myWidth = width;
//            r.myHeight = height;
//            //noinspection UseJBColor
//            r.myColor = new Color(~image.getRGB(x, y));
//            if (myEmptyRegions == null) {
//                myEmptyRegions = new ArrayList<EmptyRegion>();
//            }
//            myEmptyRegions.add(r);
//        }
//    }
//
//    protected void paintImage(Graphics g) {
//        if (myScalableImage == null) {
//            return;
//        }
//
//        double scale = panel.getZoom();
//        myScalableImage.setScale(scale);
//        myScalableImage.paint(g, 0, 0);
//
//        if (myEmptyRegions != null && !myEmptyRegions.isEmpty()) {
//            if (scale == 1) {
//                for (EmptyRegion r : myEmptyRegions) {
//                    DesignerGraphics.drawFilledRect(DrawingStyle.EMPTY, g, r.myX, r.myY, r.myWidth, r.myHeight);
//                }
//            } else {
//                for (EmptyRegion r : myEmptyRegions) {
//                    DesignerGraphics.drawFilledRect(DrawingStyle.EMPTY, g, (int) (scale * r.myX), (int) (scale * r.myY),
//                            (int) (scale * r.myWidth), (int) (scale * r.myHeight));
//                }
//            }
//        }
//    }
//
//    public int getScaledWidth() {
//        if (myScalableImage != null) {
//            myScalableImage.setScale(panel.getZoom());
//            return myScalableImage.getScaledWidth();
//        }
//
//        return 0;
//    }
//
//    public int getScaledHeight() {
//        if (myScalableImage != null) {
//            myScalableImage.setScale(panel.getZoom());
//            return myScalableImage.getScaledHeight();
//        }
//
//        return 0;
//    }
//
//    // Implements ScalableComponent
//
//    @Override
//    public double getScale() {
//        double zoom = panel.getZoom();
//        if (myScalableImage != null) {
//            Rectangle viewBounds = myScalableImage.getImageBounds();
//            if (viewBounds != null) {
//                double deviceFrameFactor = viewBounds.getWidth() / (double) myScalableImage.getScaledWidth();
//                if (deviceFrameFactor != 1) {
//                    zoom *= deviceFrameFactor;
//                }
//            }
//        }
//        return zoom;
//    }    /** Returns the height of the image itself, when scaled */
//
//
//    // Implements TransformedComponent
//
//    @Override
//    public int getShiftX() {
//        if (myScalableImage != null) {
//            Rectangle viewBounds = myScalableImage.getImageBounds();
//            if (viewBounds != null) {
//                return viewBounds.x;
//            }
//        }
//        return 0;
//    }
//
//    @Override
//    public int getShiftY() {
//        if (myScalableImage != null) {
//            Rectangle viewBounds = myScalableImage.getImageBounds();
//            if (viewBounds != null) {
//                return viewBounds.y;
//            }
//        }
//        return 0;
//    }
//
//    private static class EmptyRegion {
//        public Color myColor;
//        public int myX;
//        public int myY;
//        public int myWidth;
//        public int myHeight;
//    }
}
