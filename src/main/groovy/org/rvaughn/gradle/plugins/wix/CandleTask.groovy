package org.rvaughn.gradle.plugins.wix

import org.gradle.api.internal.ConventionTask
import org.gradle.api.tasks.*
import org.gradle.tooling.BuildException

class CandleTask extends ConventionTask {

  @InputDirectory
  File sourceDir

  @InputFile
  File harvestFile

  @OutputDirectory
  File objectDir

  String platform
  String wixBinPath
  Map<String, String> extraProperties

  @TaskAction
  void perform() {
    def sourceFiles = inputs.files.asFileTree.matching { include "**/*.wxs" }
    project.exec {
      executable "${wixBinPath}\\candle.exe"
      args '-nologo'
      args '-o', "${objectDir}\\"
      args "-I${sourceDir}"
      extraProperties.each { k, v -> args "-d${k}=${v}" }
      args '-arch', getPlatform()
      args sourceFiles
    }
    didWork = true
  }
}
