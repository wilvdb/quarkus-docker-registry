const core = require('@actions/core');

try {
    const native = core.getInput('native');
    const defaultOptions = '-B package -DskipTests=true -Dstyle.color=always -Djansi.force=true --no-transfer-progress';
    const mvnOptions = native === 'true' ? `${defaultOptions} -Dnative -Dquarkus.native.sources-only=true`: defaultOptions
    core.setOutput("mvn-options", mvnOptions);
    console.log(`=> mvn-options: ${mvnOptions}`);
} catch (error) {
    core.setFailed(error.message);
}