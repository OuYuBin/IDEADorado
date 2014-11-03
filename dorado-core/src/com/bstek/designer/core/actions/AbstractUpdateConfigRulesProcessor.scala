package com.bstek.designer.core.actions

;

/**
 * Created with IntelliJ IDEA.
 * User: robin
 * Date: 13-11-12
 * Time: 下午4:08
 * To change this template use File | Settings | File Templates.
 */

import com.bstek.designer.core.config.Dorado7RulesConfigImpl
import com.intellij.notification._
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.io.FileUtil

import javax.swing.event.HyperlinkEvent
import java.io.File

import scala.beans.BeanProperty


/**
 * 设计思路:与eclipse环境区别较大，eclipse在job处理中，提供较多job处理，合理使用即可实现功能，
 * 但在当前环境下需要通过基本线程操作来时间对应操作
 */
abstract class AbstractUpdateConfigRulesProcessor(@BeanProperty project: Project, @BeanProperty title: String, @BeanProperty message: String) {

  def run() {
    val ruleFile: File = FileUtil.findFirstThatExist(project.getBasePath() + "/.rules")
    process(ruleFile)
  }


  def process(ruleFile: File) {

    val process: Runnable = new Runnable() {
      def run() {
        val indicator = ProgressManager.getInstance().getProgressIndicator()
        try {
          if (indicator != null) {
            indicator.setIndeterminate(true)
            indicator.setFraction(0.10)
            indicator.setText("Updating dorado7 config rules...")
            Dorado7RulesConfigImpl(project)
            indicator.setFraction(1)
            indicator.setText(message)
          }
        } catch {
          case ex: Exception => ex.printStackTrace()
        }
      }
    };

    ApplicationManager.getApplication().invokeLater(new Runnable() {
      def run() {
        if (ProgressManager.getInstance().runProcessWithProgressSynchronously(process
          , title, true, project)) {
          val notificationGroup: NotificationGroup = new NotificationGroup("update.rule.group", NotificationDisplayType.STICKY_BALLOON, true)
          notificationGroup.createNotification(title, message, NotificationType.INFORMATION, new NotificationListener() {
            @Override
            def hyperlinkUpdate(notification: Notification, hyperlinkEvent: HyperlinkEvent) {
              //TODO
            }
          }).notify(null)
        }
      }
    });
  }


}
