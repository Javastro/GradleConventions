import pl.allegro.tech.build.axion.release.domain.hooks.HookContext

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
        pre(
            "fileUpdate", mapOf(
                "encoding" to "utf-8",
                "file" to rootDir.resolve("README.md").toString(),
                "pattern" to KotlinClosure2<String, HookContext, String>({ v, c -> "/${project.name}:${v}/" }),
                "replacement" to KotlinClosure2<String, HookContext, String>({ v, c -> "${project.name}:${v}" })
            )
        )

    }
}

version = scmVersion.version
