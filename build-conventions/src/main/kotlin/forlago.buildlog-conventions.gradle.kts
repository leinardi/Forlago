import com.leinardi.forlago.ext.config
import com.leinardi.forlago.ext.params
import org.gradle.api.internal.GradleInternal
import org.gradle.internal.logging.LoggingOutputInternal
import java.text.SimpleDateFormat
import java.util.Date

if (true /*config.params.saveBuildLogToFile.get()*/) {
    val datetime = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(Date())
    val buildLogDir = "${buildDir}/logs"
    mkdir(buildLogDir)
    val buildLog = File("${buildLogDir}/buildlog-${datetime}.txt")

    System.setProperty("org.gradle.color.error", "RED")

    val outputListener = StandardOutputListener { output -> buildLog.appendText(output.toString()) }
    (gradle as GradleInternal).services.get(LoggingOutputInternal::class.java).addStandardOutputListener(outputListener)
    (gradle as GradleInternal).services.get(LoggingOutputInternal::class.java).addStandardErrorListener(outputListener)
}
