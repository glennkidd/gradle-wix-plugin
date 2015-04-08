package org.rvaughn.gradle.plugins.wix

import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Sync

class WixPlugin implements Plugin<Project> {
  static final String WIX_PLUGIN_NAME = 'wix'
  static final String WIX_GROUP = WIX_PLUGIN_NAME

  static final String TASK_GATHER_NAME = 'gatherJars'
  static final String TASK_HEAT_NAME = 'heat'
  static final String TASK_CANDLE_NAME = 'candle'
  static final String TASK_LIGHT_NAME = 'light'
  static final String TASK_WIX_NAME = 'wix'

  static final String TASK_GATHER_DESC = 'Copies all runtime jars to a single directory.'
  static final String TASK_HEAT_DESC = 'Creates a WiX source file from dynamic files.'
  static final String TASK_CANDLE_DESC = 'Compile WiX source files.'
  static final String TASK_LIGHT_DESC = 'Links WiX object files into an MSI.'
  static final String TASK_WIX_DESC = 'Builds an MSI installer with WiX.'

  private Project project
  private WixExtension extension

  void apply(Project project) {
    this.project = project

    createExtension()
    createGatherTask()
    createHeatTask()
    createCandleTask()
    createLightTask()
    createWixTask()
    linkTasks()
  }

  def createExtension() {
    extension = project.extensions.create('wix', WixExtension, project)
  }

  def createGatherTask() {
    def task = project.tasks.create(TASK_GATHER_NAME, Sync)
    task.description = TASK_GATHER_DESC
    task.from(project.configurations.runtime)
    task.from(project.tasks.jar.archivePath)
    task.into({ extension.jarsDir })
    task
  }

  def createHeatTask() {
    def task = project.tasks.create(TASK_HEAT_NAME, HeatTask)
    task.description = TASK_HEAT_DESC
    task.conventionMapping.wixBinPath = { extension.binPath }
    task.conventionMapping.inputDir   = { extension.jarsDir }
    task.conventionMapping.outputFile = { extension.heatOutputFile }
    task.conventionMapping.componentGroupId = { extension.heatComponentGroupId }
    task.conventionMapping.directoryRefId   = { extension.heatDirectoryRefId }
    task.conventionMapping.sourceDirVarName = { extension.heatSourceDirVarName }
    task
  }

  def createCandleTask() {
    def task = project.tasks.create(TASK_CANDLE_NAME, CandleTask)
    task.description = TASK_CANDLE_DESC
    task.conventionMapping.wixBinPath      = { extension.binPath }
    task.conventionMapping.sourceDir       = { extension.sourceDir }
    task.conventionMapping.harvestFile     = { project.tasks[TASK_HEAT_NAME].outputFile }
    task.conventionMapping.objectDir       = { extension.objectDir }
    task.conventionMapping.platform        = { extension.platform }
    task.conventionMapping.extraProperties = { extension.properties }
    task
  }

  def createLightTask() {
    def task = project.tasks.create(TASK_LIGHT_NAME, LightTask)
    task.description = TASK_LIGHT_DESC
    task.conventionMapping.objectDir  = { extension.objectDir }
    task.conventionMapping.outputFile = { extension.outputFile }
    task.conventionMapping.wixBinPath = { extension.binPath }
    task
  }

  def createWixTask() {
    def task = project.tasks.create(TASK_WIX_NAME, DefaultTask)
    task.description = TASK_WIX_DESC
    task.group = WIX_GROUP
    task
  }

  def linkTasks() {
    def wixTask = project.tasks[TASK_WIX_NAME]
    def lightTask = project.tasks[TASK_LIGHT_NAME]
    def candleTask = project.tasks[TASK_CANDLE_NAME]
    def gatherTask = project.tasks[TASK_GATHER_NAME]
    
    wixTask.dependsOn(lightTask)
    lightTask.dependsOn(candleTask)
    
    project.tasks.withType(HeatTask) { HeatTask t ->
      candleTask.dependsOn(t)
      t.dependsOn(gatherTask)
    }

    gatherTask.dependsOn(project.tasks.jar)

    // this prevents simple builds on non-Windows platforms
    // project.tasks.assemble.dependsOn(wixTask)
  }
}
