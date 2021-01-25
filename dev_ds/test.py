import pyttsx3

def Jura_speak(text, name_file = None):
    engine = pyttsx3.init()
    voices = engine.getProperty('voices')
    engine.setProperty('voice', voices[0].id)#0 7 11
    if name_file == None:
        engine.say(text)
    else:
        print('3-')
        path = '/Users/apple/Desktop/Python/my projects/Jura/Jura-1/dev_ds/'
        engine.save_to_file(text, path + name_file)
    engine.runAndWait()

Jura_speak("'You have one mistake. \n' hiii")
