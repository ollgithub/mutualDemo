package com.mo.app

import android.graphics.PointF
import com.mo.R
import com.mo.android.TimeServer
import com.mo.android.assist.color
import com.mo.android.assist.dp
import com.mo.android.assist.dpf
import com.mo.android.interaction.VibratorServer
import com.mo.base.Color
import com.mo.base.Start
import com.mo.core.Entity
import com.mo.currentTime
import com.mo.loopSwitch
import com.mo.mutual.effect.Shadow
import com.mo.mutual.element.Circle
import com.mo.mutual.element.Empty
import com.mo.mutual.element.Rect
import com.mo.mutual.element.Text
import com.mo.mutual.input.Click
import com.mo.mutual.layout.linear.Column
import com.mo.mutual.layout.space.Space
import com.mo.mutual.orbit.TimerOrbit
import com.mo.mutual.shader.GradientShader
import com.mo.mutual.sync

@Start
class StartEntity : Entity {

    override val mutual = Space {
        var circleColor by sync(Color.RED)
        var showMutualText by sync(0, false)
        var circleSize by sync(1400)
        var circlePos by sync(PointF(0.5f, 0.4f))
        Rect()
            .shader(
                GradientShader(
                    GradientShader.GradientType.LINEAR,
                    PointF(0f, 1f), PointF(1f, 0f),
                    R.color.background_start.color(), R.color.background_end.color()
                )
            )
            .size { fill() }
            .reaction(Click {
                VibratorServer.shot()
                circleSize = loopSwitch(circleSize, 1400, 3000)
                showMutualText = loopSwitch(showMutualText, 0, 1)
                circleColor = loopSwitch(circleColor, Color.RED, Color.ORANGE, Color.WHITE)
                circlePos = loopSwitch(circlePos, PointF(0.5f, 0.4f), PointF(0.15f, 0.85f))
                true
            })

        Circle()
            .position {
                vertical(circlePos.y)
                horizontal(circlePos.x)
            }
            .size { set(circleSize) }
            .shader(
                GradientShader(
                    GradientShader.GradientType.LINEAR,
                    PointF(0f, 0f), PointF(1f, 1f),
                    circleColor, Color.TRANSPARENT
                )
            )
        Text(TimeServer.formatHMS(currentTime()), 50.dpf)
            .effect(Shadow(radio = 12f, color = R.color.text_shadow.color()))
            .shader(Color.BLACK)
            .orbit(TimerOrbit(1000))
            .position {
                horizontal()
                top(90.dp)
            }
        Column(0) {
            Text("Hello World ! ", 35.dpf)
                .effect(Shadow(radio = 12f, color = R.color.text_shadow.color()))
                .shader(Color.BLACK)
            if (showMutualText == 1) {
                Empty()
                    .size { set(100) }
                Text("Hello Mutual !", 35.dpf)
                    .effect(Shadow(radio = 12f, color = R.color.text_shadow.color()))
            }
        }.position { center() }
    }.size { fill() }
}

/**
## mutual意为<相互>,程序与用户进行交互反馈的实例

#### 核心目标:
1. 更高的界面开发效率
对标其他声明UI框架, 优化自定义View的组合实现
2. 更高的UI性能
单帧内完成布局及对标原生绘制性能
3. 更多更炫的效果
新的动画框架, 移植OpenGL获得更多绘制能力


#### 设计思想
1. 面向设计而非实现(框架应当处理好兼容性,性能，规避方案等，只面向最终效果)
2. 将 视觉交互动效 统一为Mutual的职责范围, 和 业务能力 完全分层
3. 声明响应式编程和命令更新式兼容写法(考虑是否舍弃)
4. 控件的尺寸交互等规则作为其属性的一部分, 任何一部分都作为一个独立控件存在
5. 用最基础的元素布局组合实现自定义View, 用基础布局规则完成复杂控件
6. 解耦安卓框架, 方便移植OpenGL, 后续会增加更多绘制效果
7. 面向过程的动画框架<轨道>, 将动画中的状态和起点终点分离, 解耦动画设计

#### 创新点
1. 基于单次测量完成布局的布局规则,优化原生的多次测量布局性能问题
2. 基于Lambda表达式的布局规则定义, 快速适配各种UI场景
3. 将基础绘制元素作为UI组件, 用自动布局完成自定义View的构建, 用组合而非继承实现UI
4. 基于委托实现响应式界面更新
5. 基于代码位置的UI Tree更新重建
6. 和安卓框架解耦, 方便移植到OpenGL平台实现更多效果
7. ...

#### Mutual能力:
- 绘制
形状(方向,圆形,路径,文字)
着色器(颜色,纹理,渐变,调色,维度着色)
效果(阴影,发光,毛玻璃,透镜,滤镜,)
混合(alpha,色彩混合)
- 空间占据
尺寸规则
- 位置
位置规则(由layout持有)
- 轨道
动效配置
- 行为
反应配置,处理事件
 */


