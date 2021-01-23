import telebot
import requests
import pyttsx3

import speech_recognition as sr
from pydub import AudioSegment
import tempfile



def Jura_listen(file = None):
    r = sr.Recognizer()
    #если мы хотим использовать файл, то вместо sr.Microphone() нужно sr.AudioFile(‘file.wav’)
    if file == None:
        first = sr.Microphone()
    else:
        first = sr.AudioFile(file)
    with first as source:
        print("Speak")
        audio = r.listen(source)
        r.adjust_for_ambient_noise(source, duration = 1)
        text = r.recognize_google(audio).lower()
    return text

def Jura_speak(text, name_file = None):
    engine = pyttsx3.init()
    voices = engine.getProperty('voices')
    engine.setProperty('voice', voices[0].id)#0 7 11
    if name_file == None:
        engine.say(text)
    else:
        engine.save_to_file(text, name_file)

    engine.runAndWait()


TELEGRAM_API_TOKEN = '1524034227:AAHnREYZiNMiEZEk_4F8_fA7Vt6-o3RGfxY'
bot = telebot.TeleBot(TELEGRAM_API_TOKEN)
@bot.message_handler(content_types=['voice'])


def voice_processing(message):
    file_info = bot.get_file(message.voice.file_id)
    file = requests.get('https://api.telegram.org/file/bot{0}/{1}'.format(TELEGRAM_API_TOKEN, file_info.file_path))

    f = tempfile.NamedTemporaryFile(delete=False)
    f.write(file.content)
    AudioSegment.from_ogg(f.name).export('result.wav', format='wav')
    jl = Jura_listen('result.wav')
    bot.send_message(message.from_user.id, jl)

    name_file = 'js'
    Jura_speak(jl, name_file)
    AudioSegment.from_file(name_file).export(name_file + '.ogg', format="ogg", codec='libopus')
    bot.send_voice(message.chat.id, voice=open(name_file + '.ogg', 'rb'))


bot.polling(none_stop=True, interval=0)
