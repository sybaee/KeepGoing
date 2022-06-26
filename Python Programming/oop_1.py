"""
Description: convert procedural programming to an object-oriented version 
"""

from cs1graphics import *
import time

canvas = Canvas(1000, 300) # canvas 만들기 
canvas.setBackgroundColor("light blue")
canvas.setTitle("Journey of Chicken")

# Create the scene
class Scene:
    def __init__(self):
        self.ground = Ground()
        self.sun = Sun()
        self.family = Family()

    def get_data_for_anim(self):
        return self.family.group.layer, self.family.chick2.shape, self.family.chick2.wing

# Define the ground
class Ground:
    def __init__(self):
        self.shape = Rectangle(1000, 100)
        self.shape.setFillColor("light green")
        self.shape.move(500, 250)
        canvas.add(self.shape)

# Define the sun
class Sun:
    def __init__(self):
        self.shape = Circle(50)
        self.shape.setFillColor("red")
        self.shape.move(25, 25)
        canvas.add(self.shape)

# Define the chicken family
class Family:
    def __init__(self):
        self.group = Group()
        self.group.layer.move(600, 200)
        self.chick2 = Chicken() # 엄마닭을 나중에 따라가는 병아리 
        self.chick2.shape.move(800, 200)
        canvas.add(self.group.layer) # layer에 group이 add되었기 때문에 layer로 부르기
        canvas.add(self.chick2.shape)

# Define the group
class Group:
    def __init__(self):
        self.hen = Chicken(True)
        self.chick1 = Chicken()
        self.chick1.shape.move(120, 0)
        self.layer = Layer() # Layer에 hen과 chick1을 넣어서 group으로 만들어 같이 움직이게 하기 
        self.layer.add(self.hen.shape)
        self.layer.add(self.chick1.shape)

# Define a chicken
class Chicken:
    def __init__(self, hen = False):
        self.body = Body(hen)
        self.wing = Wing(hen)
        self.eye = Eye(hen)
        self.beak = Beak(hen)
        self.dots = Dots(hen)
        self.shape = Layer()
        self.shape.add(self.body.shape)
        self.shape.add(self.wing.shape)
        self.shape.add(self.eye.shape)
        self.shape.add(self.beak.shape)
        if hen: # 엄마닭일 경우에만 벼슬 넣기 
            self.shape.add(self.dots.shape1)
            self.shape.add(self.dots.shape2)

# Define a body
class Body:
    def __init__(self, hen):
        if hen:
            self.shape = Ellipse(70, 80)
            self.shape.setFillColor("white")
        else:
            self.shape = Ellipse(40, 50)
            self.shape.setFillColor("yellow")
            self.shape.move(0, 10)
        self.shape.setBorderColor("yellow")
        self.shape.setDepth(20)

# Define a wing
class Wing:
    def __init__(self, hen):
        if hen:
            self.shape = Ellipse(60, 40)
            self.shape.setFillColor("white")
            self.shape.setBorderColor("yellow")
            self.shape.move(15, 20)
        else:
            self.shape = Ellipse(30, 20)
            self.shape.setFillColor("yellow")
            self.shape.setBorderColor("orange")
            self.shape.move(10, 20)
            self.shape.adjustReference(-5, -5)
        self.shape.setDepth(19)

    def flap(self, angle):
        self.shape.rotate(angle)

# Define an eye
class Eye:
    def __init__(self, hen):
        if hen:
            self.shape = Circle(3)
            self.shape.move(-15, -15)
        else:
            self.shape = Circle(2)
            self.shape.move(-5, 0)
        self.shape.setFillColor("black")
        self.shape.setDepth(18)

# Define a beak
class Beak:
    def __init__(self, hen):
        if hen:
            self.shape = Square(8)
            self.shape.move(-36, 0)
        else:
            self.shape = Square(4)
            self.shape.move(-22, 10)
        self.shape.rotate(45)
        self.shape.setFillColor("orange")
        self.shape.setBorderColor("orange")
        self.shape.setDepth(21)            

# Define dots
class Dots:
    def __init__(self, hen):
        if hen: # 벼슬 모양을 위해 Ellipse를 2개 만들어야 하므로 shape1과 shape2 나누기 
            self.shape1 = Ellipse(5, 8)         
            self.shape1.setFillColor("red")
            self.shape1.setBorderColor("red")
            self.shape1.move(0, -42)
            self.shape1.setDepth(22)

            self.shape2 = Ellipse(5, 8)
            self.shape2.setFillColor("red")
            self.shape2.setBorderColor("red")
            self.shape2.move(-6, -42)
            self.shape2.setDepth(22)            

# Animate chickens
def animate_chickens(scene):
    animation = Animation(scene)
    animation.move_group()
    animation.move_chicken2()

class Animation:
    def __init__(self, scene):
        group, chicken2, wing2 = scene.get_data_for_anim()
        self.group = group
        self.chicken2 = chicken2
        self.wing2 = wing2

    def move_group(self):
        for i in range(80):
            self.group.move(-5, -2)
            self.group.move(-5, 2)
            if i == 30:
                text1 = Text("OH!", 20)
                text1.move(800, 160)
                canvas.add(text1)
            elif i == 40:
                canvas.remove(text1)
                text2 = Text("WHERE IS MY MOMMY GOING?", 30)
                text2.move(500, 110)
                canvas.add(text2)
            elif i == 55:
                canvas.remove(text2)

    def move_chicken2(self):
        text3 = Text("Wait for ME~", 25)
        text3.move(500, 110)
        canvas.add(text3)
        for j in range(10):
            for i in range(5):
                self.chicken2.move(-10, -20)
                self.wing2.flap(-10)
            for i in range(5):
                self.chicken2.move(-10, 20)
                self.wing2.flap(10)
            time.sleep(0.06)
        canvas.remove(text3)
                  
def main():
    scene = Scene()
    animate_chickens(scene)
    canvas.wait()
    canvas.close()

main()