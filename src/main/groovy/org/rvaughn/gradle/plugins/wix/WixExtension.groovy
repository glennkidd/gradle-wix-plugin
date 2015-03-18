package org.rvaughn.gradle.plugins.wix

import org.gradle.api.Project

class WixExtension {

  final Project project

  File inputDir
  File harvestDir
  File harvestFile
  File objectDir
  File outputDir
  File sourceDir
  File outputFile
  String binPath
  String platform
  String version

  WixExtension(Project project) {
    this.project = project
    inputDir = new File("${project.buildDir}\\alljars")
    harvestDir = new File("${project.buildDir}\\wix\\src")
    harvestFile = new File(harvestDir, "jars.wxs")
    objectDir = new File("${project.buildDir}\\wix\\obj")
    outputDir = new File("${project.buildDir}\\distributions")
    sourceDir = new File("src\\main\\install")
    outputFile = new File(outputDir, "${project.name}-${project.version}.msi")
    binPath = "${System.getenv('WIX')}bin"
    platform = 'x64'
    version = project.version
  }
}
