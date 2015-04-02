package org.rvaughn.gradle.plugins.wix

import org.gradle.api.internal.ConventionTask
import org.gradle.api.tasks.*

class HeatTask extends ConventionTask {

  @InputDirectory
  File inputDir

  @OutputFile
  File outputFile

  String componentGroupId
  String directoryRefId
  String sourceDirVarName
  String wixBinPath

  HeatTask() {
    super()
  }

  @TaskAction
  void perform() {
    project.exec {
      executable "${wixBinPath}\\heat.exe"
      args 'dir', getInputDir()
      args '-nologo'
      args '-o', getOutputFile()
      args '-gg'
      args '-cg', getComponentGroupId()
      args '-dr', getDirectoryRefId()
      args '-var', getSourceDirVarName()
      args '-scom', '-sreg', '-sfrag', '-srd'
    }
    didWork = true
  }
}
