import pl.allegro.tech.build.axion.release.domain.hooks.HookContext
import pl.allegro.tech.build.axion.release.domain.scm.ScmPosition

// Versions managed by axion-release-plugin
plugins {
    id("pl.allegro.tech.build.axion-release")
}

scmVersion {
    tag {
        initialVersion({ c, p -> "0.9.0" })
        prefix.set(project.name)
        versionSeparator.set("_")
    }
    hooks {
        post(
            "fileUpdate", mapOf(
                "encoding" to "utf-8",
                "file" to rootDir.resolve("README.md").toString(),
                "pattern" to KotlinClosure2<String, HookContext, String>({ v, c -> "${project.name}:${v}" }),
                "replacement" to KotlinClosure2<String, HookContext, String>({ v, c -> "${project.name}:${v}" })
            )
        )
        post("commit" , KotlinClosure2<String,ScmPosition,String>({ v, p-> "Updating README for version ${v}" }))


    }
}

version = scmVersion.version
