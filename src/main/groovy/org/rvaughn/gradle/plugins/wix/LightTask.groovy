package org.rvaughn.gradle.plugins.wix

import org.gradle.api.internal.ConventionTask
import org.gradle.api.tasks.*

class LightTask extends ConventionTask {

  @InputDirectory
  File objectDir

  @OutputFile
  File outputFile

  String wixBinPath

  @TaskAction
  void perform() {
    project.exec {
      executable "${wixBinPath}\\light.exe"
      args '-nologo'
      args '-o', getOutputFile()
      args '-ext', "${wixBinPath}\\WixUtilExtension.dll"
      args inputs.getFiles()
    }
    didWork = true
  }
}
