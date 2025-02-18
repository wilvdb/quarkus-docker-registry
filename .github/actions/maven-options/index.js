const core = require('@actions/core');

try {
    const native = core.getInput('native');
    const defaultOptions = '-B package -DskipTests=true -Dstyle.color=always -Djansi.force=true --no-transfer-progress';
    const mvnOptions = native === 'true' ? `${defaultOptions} -Dnative -Dquarkus.native.sources-only=true`: defaultOptions;
    core.setOutput("mvn-options", mvnOptions);
    const targets = native === 'true' ? 'registry/target/**/*.jar': 'registry/target/quarkus-app/**/*.jar';
    core.setOutput("targets", targets);
    const artifactsName = native === 'true' ? 'native-jars': 'jars';
    core.setOutput("artifacts-name", artifactsName);
    console.log(`=> mvn-options: ${mvnOptions}`);
    console.log(`=> targets: ${targets}`);
    console.log(`=> artifacts-name: ${artifactsName}`);
} catch (error) {
    core.setFailed(error.message);
}