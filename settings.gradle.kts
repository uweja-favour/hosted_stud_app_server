plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "backend-root"

include(
    "self-test-quiz-service",
    "common:file_manager",
    "auth-service"
)

include("contracts")
include("platform")
include("question-generator-service")
include("realtime-gateway")
include("classroom-service")
include("classroom-quiz-service")
include("note-summary-service")