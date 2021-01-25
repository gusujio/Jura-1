import telebot
import requests
import pyttsx3
import os

import speech_recognition as sr
from pydub import AudioSegment
import tempfile

import language_tool_python as lt
from chatterbot import ChatBot
from chatterbot.trainers import ChatterBotCorpusTrainer

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
        print(text, name_file)
        path = '/Users/apple/Desktop/Python/my projects/Jura/Jura-1/dev_ds/'
        engine.save_to_file(text, path + name_file)
    engine.runAndWait()


def Jura_correct(text):
    tool = lt.LanguageToolPublicAPI('en-US')
    matches = tool.check(text)
    number_mistakes = len(matches)
    if number_mistakes == 0:
        return None
    else:
        if number_mistakes == 1:
            s = 'You have one mistake,  \n'
        else:
            s = 'You have ' + str(number_mistakes) + ' mistakes,  \n'

        s += 'I think you should have said, \n'

        t2 = tool.correct(text)
        s += t2
        return s

def ft_input(message):
    file_info = bot.get_file(message.voice.file_id)
    file = requests.get('https://api.telegram.org/file/bot{0}/{1}'.format(TELEGRAM_API_TOKEN, file_info.file_path))

    f = tempfile.NamedTemporaryFile(delete=False)
    f.write(file.content)
    AudioSegment.from_ogg(f.name).export('result.wav', format='wav')
    jl = Jura_listen('result.wav')
    # bot.send_message(message.from_user.id, jl)
    return jl

def ft_output(message, answer):
    name_file = 'js'
    # print(message, user_input, name_file)
    Jura_speak(answer, name_file)
    AudioSegment.from_file(name_file).export(name_file + '.ogg', format="ogg", codec='libopus')
    bot.send_voice(message.chat.id, voice=open(name_file + '.ogg', 'rb'))
    os.remove(name_file)
    os.remove(name_file + '.ogg')


TELEGRAM_API_TOKEN = '1524034227:AAHnREYZiNMiEZEk_4F8_fA7Vt6-o3RGfxY'
bot = telebot.TeleBot(TELEGRAM_API_TOKEN)
@bot.message_handler(content_types=['voice'])

def voice_processing(message):
    user_input = ft_input(message)

    chatbot = ChatBot('Ron Obvious')
    # trainer = ChatterBotCorpusTrainer(chatbot)
    # trainer.train("/Users/apple/chatterbot-corpus1/chatterbot_corpus/data/english")

    corect = Jura_correct(user_input)
    if corect != None:
        bot.send_message(message.from_user.id, 'Исправление ошибок:')
        ft_output(message, corect)
        bot.send_message(message.from_user.id, 'Ответ бота:')

    bot_output = chatbot.get_response(user_input)
    ft_output(message, str(bot_output))



bot.polling(none_stop=True, interval=0)
