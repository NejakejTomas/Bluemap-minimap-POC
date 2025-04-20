package cz.nejakejtomas.bluemapminimap.render

//import me.x150.renderer.render.ClipStack
import com.mojang.blaze3d.vertex.PoseStack

fun PoseStack.withPose(function: PoseStack.() -> Unit) {
    pushPose()
    function()
    popPose()
}

//fun PoseStack.withWindow(rectangle: Rectangle, function: () -> Unit) {
//    ClipStack.use(this, rectangle, function)
//}
