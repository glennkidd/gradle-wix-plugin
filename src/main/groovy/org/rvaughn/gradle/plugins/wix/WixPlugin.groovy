package org.rvaughn.gradle.plugins.wix

import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project

class WixPlugin implements Plugin<Project> {
  static final String WIX_PLUGIN_NAME = 'wix'
  static final String WIX_GROUP = WIX_PLUGIN_NAME

  static final String TASK_HEAT_NAME = 'heat'
  static final String TASK_CANDLE_NAME = 'candle'
  static final String TASK_LIGHT_NAME = 'light'
  static final String TASK_WIX_NAME = 'wix'

  static final String TASK_HEAT_DESC = 'Creates a WiX source file from dynamic files.'
  static final String TASK_CANDLE_DESC = 'Compile WiX source files.'
  static final String TASK_LIGHT_DESC = 'Links WiX object files into an MSI.'
  static final String TASK_WIX_DESC = 'Builds an MSI installer with WiX.'

  private Project project
  private WixExtension extension
  
  void apply(Project project) {
    this.project = project

    createExtension()
    createHeatTask()
    createCandleTask()
    createLightTask()
    createWixTask()
  }

  def createExtension() {
    // conventions go into the project scope - not namespaced!
    // convention = new WixConvention(project)
    // project.convention.plugins.wix = convention
    extension = project.extensions.create('wix', WixExtension, project)
  }

  def createHeatTask() {
    def task = project.tasks.create(TASK_HEAT_NAME, HeatTask)
    task.description = TASK_HEAT_DESC
    //task.group = WIX_GROUP
    task.conventionMapping.inputDir = { extension.inputDir }
    task.conventionMapping.outputFile = { extension.harvestFile }
    task.conventionMapping.wixBinPath = { extension.binPath }
    task
  }

  def createCandleTask() {
    def task = project.tasks.create(TASK_CANDLE_NAME, CandleTask)
    task.description = TASK_CANDLE_DESC
    //task.group = WIX_GROUP
    task.dependsOn TASK_HEAT_NAME
    task.conventionMapping.sourceDir = { extension.sourceDir }
    task.conventionMapping.harvestFile = { project.tasks[TASK_HEAT_NAME].outputFile }
    task.conventionMapping.objectDir = { extension.objectDir }
    //  inputs.file project.fileTree(dir: project.wix.sourceDir, include: '**/*.wxs')
    task.conventionMapping.jarsDir = { extension.inputDir }
    task.conventionMapping.version = { extension.version }
    task.conventionMapping.platform = { extension.platform }
    task.conventionMapping.wixBinPath = { extension.binPath }
    task
  }

  def createLightTask() {
    def task = project.tasks.create(TASK_LIGHT_NAME, LightTask)
    task.description = TASK_LIGHT_DESC
    //task.group = WIX_GROUP
    task.dependsOn TASK_CANDLE_NAME
    task.conventionMapping.objectDir = { extension.objectDir }
    task.conventionMapping.outputFile = { extension.outputFile }
    task.conventionMapping.wixBinPath = { extension.binPath }
    task
  }

  def createWixTask() {
    def task = project.tasks.create(TASK_WIX_NAME, DefaultTask)
    task.description = TASK_WIX_DESC
    task.group = WIX_GROUP
    task.dependsOn TASK_LIGHT_NAME
    task
  }

  // TODO: set up task dependencies after config instead of inline
  // TODO: clean up convention names
  // TODO: filter wxs source files in inputs
  // TODO: check tool results explicitly
  // TODO: use properties in Candle
}
