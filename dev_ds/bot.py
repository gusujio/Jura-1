from chatterbot import ChatBot
from chatterbot.trainers import ChatterBotCorpusTrainer
import language_tool_python as lt
import pyttsx3

chatbot = ChatBot('Ron Obvious')

# Create a new trainer for the chatbot
trainer = ChatterBotCorpusTrainer(chatbot)

# Train the chatbot based on the english corpus
trainer.train("/Users/apple/chatterbot-corpus1/chatterbot_corpus/data/english")

# Get a response to an input statement

print(chatbot.get_response("Hello, how are you today?"))
