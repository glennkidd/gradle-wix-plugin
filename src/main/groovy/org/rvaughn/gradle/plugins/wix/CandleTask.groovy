package org.rvaughn.gradle.plugins.wix

import org.gradle.api.internal.ConventionTask
import org.gradle.api.tasks.*
import org.gradle.tooling.BuildException

class CandleTask extends ConventionTask {

  @InputDirectory
  File sourceDir

  @InputFile
  File harvestFile

  // should we map the individual wixobj files instead?
  @OutputDirectory
  File objectDir

  File jarsDir
  String version
  String platform
  String wixBinPath

  @TaskAction
  void perform() {
    def sourceFiles = inputs.files.asFileTree.matching { include "**/*.wxs" }
    project.exec {
      executable "${wixBinPath}\\candle.exe"
      args '-nologo'
      args '-o', "${objectDir}\\"
      args "-I${sourceDir}"
      args "-dJarDir=${jarsDir}"
      args "-dVersion=${version}"
      args '-arch', getPlatform()
      args sourceFiles
    }
    didWork = true
  }
}
