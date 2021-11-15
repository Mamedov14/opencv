# import the necessary packages
# импортируем необходимые пакеты
from collections import deque
import numpy as np
import argparse
import imutils
import cv2

# it is better to pass explicit object values here!
# тут лучше передать явные значения объекта!
blue = 0
green = 255
red = 0
color = np.uint8([[[blue, green, red]]])
hsv_color = cv2.cvtColor(color, cv2.COLOR_BGR2HSV)
hue = hsv_color[0][0][0]

# construct the argument parse and parse the arguments
# построить аргумент, синтаксический анализ и синтаксический анализ аргументов
ap = argparse.ArgumentParser()
ap.add_argument("-v", "--video",
                help="path to the (optional) video file")
ap.add_argument("-b", "--buffer", type=int, default=64,
                help="max buffer size")
args = vars(ap.parse_args())

# define the lower and upper boundaries of the "yellow object"
# (or "ball") in the HSV color space, then initialize the
# list of tracked points
# определяем нижнюю и верхнюю границы «объекта»
# (или "мяч") в цветовом пространстве HSV, затем инициализируйте
# список отслеживаемых точек
Lower = int(hue - 10)
Upper = int(hue + 10)
colorLower = (Lower, 100, 100)
colorUpper = (Upper, 255, 255)
# pts = deque(maxlen=args["buffer"])
pts = deque([])

# if a video path was not supplied, grab the reference
# to the webcam
# если путь к видео не был указан, возьмите ссылку
# к веб-камере
if not args.get("video", False):
    camera = cv2.VideoCapture(0)

# otherwise, grab a reference to the video file
# в противном случае получить ссылку на видеофайл
else:
    camera = cv2.VideoCapture(args["video"])

# keep looping
# продолжать цикл
while True:
    # grab the current frame
    # захватить текущий кадр
    # grabbed - это захват
    (grabbed, frame) = camera.read()

    # if we are viewing a video and we did not grab a frame,
    # then we have reached the end of the video
    # если мы просматриваем видео и не захватили кадр,
    # то мы дошли до конца видео
    if args.get("video") and not grabbed:
        break

    # resize the frame, inverted ("vertical flip" w/ 180degrees),
    # blur it, and convert it to the HSV color space
    # изменить размер кадра, перевернуть ("переворот по вертикали" на 180 градусов),
    # размыть его и преобразовать в цветовое пространство HSV
    frame = imutils.resize(frame, width=700)
    frame = cv2.flip(frame, 1)
    # frame = imutils.rotate(frame, angle=180)
    # blurred = cv2.GaussianBlur(frame, (11, 11), 0)
    hsv = cv2.cvtColor(frame, cv2.COLOR_BGR2HSV)

    # construct a mask for the color "green", then perform
    # a series of dilations and erosions to remove any small
    # blobs left in the mask
    # построить маску для цвета, затем выполнить
    # серию расширений и эрозий для удаления любых мелких
    # ляпов
    mask = cv2.inRange(hsv, colorLower, colorUpper)
    mask = cv2.erode(mask, None, iterations=2)
    mask = cv2.dilate(mask, None, iterations=2)

    # find contours in the mask and initialize the current
    # (x, y) center of the ball
    # находим контуры в маске и инициализируем текущий
    # (x, y) центр мяча

    # RETR_EXTERNAL — выдаёт только крайние внешние контуры.
    # Например, если в кадре будет пончик,
    # то функция вернет его внешнюю границу без дырки.

    # CV_CHAIN_APPROX_SIMPLE — склеивает все горизонтальные,
    # вертикальные и диагональные контуры.

    contours = cv2.findContours(mask.copy(), cv2.RETR_EXTERNAL,
                                cv2.CHAIN_APPROX_SIMPLE)[-2]
    center = None

    # only proceed if at least one contour was found
    # продолжаем только в том случае, если был найден хотя бы один контур
    if len(contours) > 0:
        # find the largest contour in the mask, then use
        # it to compute the minimum enclosing circle and
        # centroid
        # найти самый большой контур в маске, затем использовать
        # это для вычисления минимального окружающего круга и
        # центроид
        c = max(contours, key=cv2.contourArea)
        ((x, y), radius) = cv2.minEnclosingCircle(c)
        M = cv2.moments(c)
        center = (int(M["m10"] / M["m00"]), int(M["m01"] / M["m00"]))

        # only proceed if the radius meets a minimum size
        # продолжаем, только если радиус соответствует минимальному размеру
        if radius > 10:
            # draw the circle and centroid on the frame,
            # then update the list of tracked points
            # рисуем круг и центроид на кадре,
            # затем обновить список отслеживаемых точек
            cv2.circle(frame, (int(x), int(y)), int(radius),
                       (0, 255, 255), 2)
            cv2.circle(frame, center, 5, (0, 0, 255), -1)

    # update the points queue
    # обновить очередь точек
    pts.appendleft(center)

    # loop over the set of tracked points
    # перебрать набор отслеживаемых точек
    for i in range(1, len(pts)):
        # if either of the tracked points are None, ignore
        # them
        # если любая из отслеживаемых точек - None, игнорировать
        # их
        if pts[i - 1] is None or pts[i] is None:
            continue

        # otherwise, compute the thickness of the line and
        # draw the connecting lines
        # в противном случае вычислить толщину линии и
        # рисуем соединительные линии
        # thickness = int(np.sqrt(args["buffer"] / float(i + 1)) * 2.5)
        cv2.line(frame, pts[i - 1], pts[i], (0, 0, 255), 3)

    # show the frame to our screen
    # показать рамку на наш экран
    cv2.imshow("Frame", frame)
    cv2.imshow("HSV", hsv)

    key = cv2.waitKey(1) & 0xFF

    # if the 'q' key is pressed, stop the loop
    # если нажата клавиша 'q', остановить цикл
    k = cv2.waitKey(5) & 0xFF
    if k == 27:
        break

# cleanup the camera and close any open windows
# очистить камеру и закрыть все открытые окна
camera.release()
cv2.destroyAllWindows()
