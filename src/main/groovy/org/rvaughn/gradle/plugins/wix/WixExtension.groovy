package org.rvaughn.gradle.plugins.wix

import org.gradle.api.Project

class WixExtension {

  final Project project

  // heat
  File jarsDir
  File generatedSourceDir
  String heatOutputFileName
  File heatOutputFile

  // candle
  File sourceDir
  File objectDir
  String platform
  String version

  // light
  File outputDir
  String outputBaseName
  String outputExtension
  String outputFileName
  File outputFile

  // all
  String binPath

  WixExtension(Project project) {
    this.project = project

    binPath = "${System.getenv('WIX')}bin"

    // no default for jarsDir
    generatedSourceDir = new File("${project.buildDir}\\wix\\src")
    heatOutputFileName = 'gen.wxs'
    
    sourceDir = new File("src\\main\\install")
    objectDir = new File("${project.buildDir}\\wix\\obj")
    platform = 'x64'
    version = '1.0.0.1'
    
    outputDir = new File("${project.buildDir}\\distributions")
    outputBaseName = "${project.name}"
    outputExtension = "msi"
  }

  File getHeatOutputFile() {
    if (heatOutputFile)
      return heatOutputFile
    else
      heatOutputFile = new File(getGeneratedSourceDir(), getHeatOutputFileName())
  }

  String getOutputFileName() {
    if (outputFileName)
      return outputFileName
    else
      return "${outputBaseName}-${version}.${outputExtension}"
  }

  File getOutputFile() {
    if (outputFile)
      return outputFile
    else
      return new File(outputDir, getOutputFileName())
  }
}
