rootProject.name = "com.ngccorp.difficulty"

plugins {
  // See documentation on https://scaffoldit.dev
  id("dev.scaffoldit") version "0.2.+"
  id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

// Would you like to do a split project?
// Create a folder named "common", then configure details with `common { }`

hytale {
  usePatchline("pre-release")
  useVersion("latest")

  repositories {
    // Any external repositories besides: MavenLocal, MavenCentral, HytaleMaven, and CurseMaven
  }

  dependencies {
    // HyUI — admin UI framework (https://www.curseforge.com/hytale/mods/hyui)
    implementation("curse.maven:hyui-1431415:7731691")
  }

  manifest {
    Group = "com.ngccorp.difficulty"
    Name = "NGC Corp. - Difficulty"
    Main = "com.ngccorp.Difficulty"
  }
}
