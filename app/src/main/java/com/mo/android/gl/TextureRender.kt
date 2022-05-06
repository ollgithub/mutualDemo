package com.mo.android.gl;

import android.content.Context
import android.graphics.BitmapFactory
import android.opengl.GLES20
import android.opengl.GLUtils
import android.opengl.Matrix
import android.util.Log
import androidx.core.graphics.scale
import com.mo.R
import com.mo.android.rBitmap
import com.mo.android.gl.RendererManager.Companion.checkError
import com.mo.android.gl.RendererManager.Companion.loadShader
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class TextureRender(val context: Context) : SeekBarRenderer() {

    companion object {
        private const val VERTEX_SHADER: String = ("""
uniform mat4 uMVPMatrix;
varying vec2 v_texCoord;

attribute vec4 vPosition;
//attribute vec2 aPosition;
attribute vec2 a_texCoord;

void main() {
//    vec4 pos = vec4(aPosition, 0.0, 1.0);
    gl_Position = uMVPMatrix * vPosition;
    v_texCoord = a_texCoord;
}
""")
        private const val FRAGMENT_SHADER = ("""
precision mediump float;
varying vec2 v_texCoord;

uniform float mixProcess;
uniform sampler2D s_texture1;
uniform sampler2D s_texture2;

void main() {
    gl_FragColor = mix(texture2D(s_texture1, v_texCoord),texture2D(s_texture2, v_texCoord),mixProcess);
}
""")
        private val VERTEX =
            floatArrayOf(
                1f, 1.5f, 0f,
                -1f, 1.5f, 0f,
                -1f, -1.5f, 0f,
                1f, -1.5f, 0f
            )
        private val VERTEX_INDEX = shortArrayOf(0, 1, 2, 0, 2, 3)

    }

    private val vertexBuffer: FloatBuffer
    private val mTexVertexBuffer: FloatBuffer
    private val mVertexIndexBuffer: ShortBuffer
    private var program = 0
    private var positionHandle = 0
    private var mTexCoordHandle = 0
    private var mTexSampler1Handle = 0
    private var mTexSampler2Handle = 0
    private var mixProcessHandle = 0
    private var matrixHandle = 0
    private var mVPMatrix: FloatArray = FloatArray(16)


    val texNames = IntArray(2)
    var surfaceSize = IntArray(2)
    private val TEX_VERTEX = floatArrayOf(
        1f, 0f,
        0f, 0f,
        0f, 1f,
        1f, 1f
    )
    private val VERTEX_INDEX = shortArrayOf(0, 1, 2, 0, 2, 3)

    init {
        vertexBuffer = ByteBuffer.allocateDirect(VERTEX.size * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .put(VERTEX)
        vertexBuffer.position(0)
        checkError()
        mVertexIndexBuffer = ByteBuffer.allocateDirect(VERTEX_INDEX.size * 2)
            .order(ByteOrder.nativeOrder())
            .asShortBuffer()
            .put(VERTEX_INDEX);
        mVertexIndexBuffer.position(0);
        checkError()
        mTexVertexBuffer = ByteBuffer.allocateDirect(TEX_VERTEX.size * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .put(TEX_VERTEX)
        mTexVertexBuffer.position(0)
        checkError()
    }

    override fun onSurfaceCreated(unused: GL10?, config: EGLConfig?) {
        Log.d("tttt", "onSurfaceCreated ")
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f)
        checkError()
        program = GLES20.glCreateProgram()
        val vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, VERTEX_SHADER)
        val fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, FRAGMENT_SHADER)
        GLES20.glAttachShader(program, vertexShader)
        GLES20.glAttachShader(program, fragmentShader)
        GLES20.glLinkProgram(program)
        GLES20.glUseProgram(program)

        positionHandle = GLES20.glGetAttribLocation(program, "vPosition")
        mTexCoordHandle = GLES20.glGetAttribLocation(program, "a_texCoord")
        matrixHandle = GLES20.glGetUniformLocation(program, "uMVPMatrix")
        mTexSampler1Handle = GLES20.glGetUniformLocation(program, "s_texture1")
        mTexSampler2Handle = GLES20.glGetUniformLocation(program, "s_texture2")
        mixProcessHandle = GLES20.glGetUniformLocation(program, "mixProcess")
        checkError()
        GLES20.glEnableVertexAttribArray(positionHandle)
        GLES20.glVertexAttribPointer(
            positionHandle, 3, GLES20.GL_FLOAT, false,
            12, vertexBuffer
        )
        checkError()
        GLES20.glEnableVertexAttribArray(mTexCoordHandle)
        GLES20.glVertexAttribPointer(
            mTexCoordHandle, 2, GLES20.GL_FLOAT, false, 0,
            mTexVertexBuffer
        )
        checkError()
        imagePre()
//        image2Pre()

    }

    val b = R.drawable.background.rBitmap().scale(1080, 2400, true)

    fun imagePre() {
        GLES20.glGenTextures(2, texNames, 0)
        Log.e("tttt", "texName $texNames")
        val bitmap = b
//        BitmapFactory.decodeResource(
//            context.resources,
//            R.drawable.cy
//        )
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texNames[0])
        GLES20.glTexParameteri(
            GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,
            GLES20.GL_LINEAR
        )
        GLES20.glTexParameteri(
            GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER,
            GLES20.GL_LINEAR
        )
        GLES20.glTexParameteri(
            GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,
            GLES20.GL_REPEAT
        )
        GLES20.glTexParameteri(
            GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,
            GLES20.GL_REPEAT
        )
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0)
        checkError()
    }

    fun image2Pre() {
        val bitmap = BitmapFactory.decodeResource(
            context.resources,
            R.drawable.background
        )
        GLES20.glActiveTexture(GLES20.GL_TEXTURE1)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texNames[1])
        GLES20.glTexParameteri(
            GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,
            GLES20.GL_LINEAR
        )
        GLES20.glTexParameteri(
            GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER,
            GLES20.GL_LINEAR
        )
        GLES20.glTexParameteri(
            GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,
            GLES20.GL_REPEAT
        )
        GLES20.glTexParameteri(
            GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,
            GLES20.GL_REPEAT
        )
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0)
        checkError()
        bitmap.recycle()

    }

    override fun onSurfaceChanged(unused: GL10?, width: Int, height: Int) {
        Log.d("tttt", "onSurfaceChanged w $width h $height")
        GLES20.glViewport(0, 0, width, height)
        surfaceSize = intArrayOf(width, height)
        updateMatrix()
        checkError()
    }

    fun updateMatrix() {
        Matrix.perspectiveM(
            mVPMatrix,
            0,
            progress[0] * 200f,
            surfaceSize[0].toFloat() / surfaceSize[1],
            0.1f,
            100f
        )
        Matrix.translateM(mVPMatrix, 0, 0f, 0f, -2.5f)
    }

    override fun onDrawFrame(unused: GL10?) {
        Log.d("tttt", "onDrawFrame ")


        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        updateMatrix()
        GLES20.glUniformMatrix4fv(matrixHandle, 1, false, mVPMatrix, 0)
        GLES20.glUniform1i(mTexSampler1Handle, 0)
        GLES20.glUniform1i(mTexSampler2Handle, 1)
        GLES20.glUniform1f(mixProcessHandle, progress[1])
        checkError()
        // 用 glDrawElements 来绘制，mVertexIndexBuffer 指定了顶点绘制顺序
        GLES20.glDrawElements(
            GLES20.GL_TRIANGLES, VERTEX_INDEX.size,
            GLES20.GL_UNSIGNED_SHORT, mVertexIndexBuffer
        )

        checkError()
    }

    override fun onDestroy() {
        super.onDestroy()
        GLES20.glDeleteTextures(1, texNames, 0)
    }

}