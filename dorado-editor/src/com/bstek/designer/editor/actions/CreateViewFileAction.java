package com.bstek.designer.editor.actions;

import com.intellij.CommonBundle;
import com.intellij.openapi.fileTypes.StdFileTypes;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.ui.DocumentAdapter;
import icons.DoradoIcons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created with IntelliJ IDEA.
 * User: robin
 * Date: 13-9-29
 * Time: 下午3:24
 * To change this template use File | Settings | File Templates.
 */
public class CreateViewFileAction extends AbstractCreateViewFileAction {

    public CreateViewFileAction() {
        super("Dorado7 View", "Create new Dorado7 View File", DoradoIcons.DORADO7_VIEW_FILE);
    }

    @NotNull
    @Override
    protected PsiElement[] invokeDialog(Project project, PsiDirectory directory) {
        final MyInputValidator validator = new MyInputValidator(project, directory);
        final DialogWrapper dialog = new CreateViewDialog(project, validator);
        dialog.show();
        return validator.getCreatedElements();
    }

    @NotNull
    @Override
    protected PsiElement[] create(String newName, PsiDirectory directory) throws Exception {
        PsiElement createdFile;
        PsiElement newJsController;
        final String viewBody = createViewBody("/com/bstek/designer/common/NewView.view.xml");
        PsiFile viewFile = PsiFileFactory.getInstance(directory.getProject()).createFileFromText(newName + ".view.xml", StdFileTypes.XML, viewBody);
        createdFile = directory.add(viewFile);
        return new PsiElement[]{createdFile};
    }

    @Override
    protected String getErrorTitle() {
        return CommonBundle.getErrorTitle();
    }

    @Override
    protected String getCommandName() {
        return "View File";
    }

    @Override
    protected String getActionName(PsiDirectory directory, String newName) {
        return "New Dorado7 View";  //To change body of implemented methods use File | Settings | File Templates.
    }

    public class CreateViewDialog extends DialogWrapper {

        private JTextField viewNameTextField;
        private JCheckBox createBoundJsControllerCheckBox;
        private JTextField jsControllerNameTextField;
        private JPanel myTopPanel;
        private final Project project;
        private final MyInputValidator validator;

        protected CreateViewDialog(@Nullable Project project, MyInputValidator validator) {
            super(project, true);
            this.project = project;
            this.validator = validator;
            init();
            setTitle("New Dorado7 View");
            setOKActionEnabled(false);

            createBoundJsControllerCheckBox.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    if (createBoundJsControllerCheckBox.isSelected()) {
                        if (!viewNameTextField.getText().equals("")) {
                            jsControllerNameTextField.setText(viewNameTextField.getText());
                        }
                    } else {
                        jsControllerNameTextField.setEnabled(false);
                    }
                }
            });


            viewNameTextField.getDocument().addDocumentListener(new DocumentAdapter() {
                protected void textChanged(DocumentEvent e) {
                    setOKActionEnabled(viewNameTextField.getText().length() > 0);
                    if (createBoundJsControllerCheckBox.isSelected()) {
                        jsControllerNameTextField.setText(viewNameTextField.getText());
                    }
                }
            });
        }

        @Nullable
        @Override
        protected JComponent createCenterPanel() {
            return myTopPanel;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        protected void doOKAction() {
            String inputString = viewNameTextField.getText().trim();
            if (validator.canClose(inputString)) {
                close(OK_EXIT_CODE);
            }
        }
    }
}
