import speech_recognition as sr
import  string


class Jura_books:
	#     true_text - правильный текст, который из книги
	#     file - файл с аудио дорожкой пользователя, если его нет, то значит запись происходит online
	#     метод false_text - текст, который предсказала нейронка
	#     метод words_dict - словарь с словами из текста и пометка о том, правильно или нет их прочел пользователь

	def __init__(self, true_text=None, file=None):
		self.true_text = true_text
		self.file = file

	def recognizer_file(self):
		r = sr.Recognizer()
		# если мы хотим использовать файл, то вместо sr.Microphone() нужно sr.AudioFile(‘file.wav’)
		if self.file == None:
			first = sr.Microphone()
		else:
			first = sr.AudioFile(file)
		with first as source:
			print("Speak")
			audio = r.listen(source)
			r.adjust_for_ambient_noise(source, duration=1)
			text = r.recognize_google(audio).lower()
		return text

	def recognizer_text(self):
		words_dict = {}
		tt = str.maketrans(dict.fromkeys(string.punctuation))
		text = self.true_text.translate(tt)
		for word in text.split():
			if word in self.false_text:
				words_dict[word] = 'green'
			else:
				words_dict[word] = 'red'
		return words_dict

	def jura_recognizer(self, true_text=None, file=None):
		if true_text != None:
			self.true_text = true_text
		if file != None:
			self.file = file

		false_text = self.recognizer_file()
		self.false_text = false_text
		words_dict = self.recognizer_text()
		self.words_dict = words_dict
		return self.words_dict

# Пример использования 
true_text = 'Somewhere a telephone was ringing.'
jura = Jura_books()
print(jura.jura_recognizer(true_text))

print(jura.words_dict)