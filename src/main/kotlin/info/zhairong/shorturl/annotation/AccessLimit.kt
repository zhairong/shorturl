package info.zhairong.shorturl.annotation

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy


@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@Retention(
    RetentionPolicy.RUNTIME
)
annotation class AccessLimit( // max access count.
    val maxCount: Int,  // message for too much access.
    val msg: String = "too many tries!"
)
