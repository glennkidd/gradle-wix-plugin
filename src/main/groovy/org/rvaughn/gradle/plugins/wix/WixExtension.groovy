package org.rvaughn.gradle.plugins.wix

import org.gradle.api.Project

class WixExtension {

  final Project project

  // gather/heat
  File jarsDir

  // heat
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
  Map<String, String> properties

  WixExtension(Project project) {
    this.project = project
    properties = new HashMap<String, String>()

    binPath = "${System.getenv('WIX')}bin"

    jarsDir = new File("${project.buildDir}\\wix\\jars")
    generatedSourceDir = new File("${project.buildDir}\\wix\\src")
    heatOutputFileName = 'gen.wxs'
    
    sourceDir = new File("src\\main\\install")
    objectDir = new File("${project.buildDir}\\wix\\obj")
    platform = 'x64'
    version = project.version == 'unspecified' ? '1.0.0.1' : project.version
    
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

  void setProperties(Map<String, String> newProps) {
    properties.putAll(newProps)
  }
}
