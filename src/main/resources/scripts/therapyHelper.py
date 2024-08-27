import os, sys
import json
from dotenv import load_dotenv
from langchain.chat_models import ChatOpenAI
from langchain.prompts import PromptTemplate
from langchain.embeddings.openai import OpenAIEmbeddings
from langchain.text_splitter import RecursiveCharacterTextSplitter
from langchain.vectorstores import FAISS
from langchain.chains import LLMChain
from langchain_community.document_loaders import TextLoader


load_dotenv()
openai_api_key = os.getenv("OPENAI_API_KEY")

patient_id = sys.argv[1]
input = sys.argv[2]
chat_history = sys.argv[3]
user_language = sys.argv[4]

loader = TextLoader("src/main/resources/scripts/essay_style_therapy_type.txt")
docsFull = loader.load()

text_splitter = RecursiveCharacterTextSplitter(chunk_size=500, chunk_overlap=0)
therapies = text_splitter.split_documents(docsFull)

embeddings = OpenAIEmbeddings(openai_api_key=openai_api_key)

db = FAISS.from_documents(therapies, embeddings)

docs = db.similarity_search(input, k=8)
docs_page_content = " ".join([d.page_content for d in docs])

llm = ChatOpenAI(temperature=0.3, model_name = 'gpt-4o', max_tokens=1024, openai_api_key=openai_api_key)

prompt = PromptTemplate(
    input_variables=["patient_id", "input", "docs", "chat_history"],
    template="""
        You are a friendly chatbot for Medical Rehabilitation system that gives info about therapies or therapy recommendations to patients based on their symptoms. 

        Patient's name: {patient_id}. 
        Patient's question: {input}.
        Create clear, empathetic answer by searching for information on user input here: {docs}

        Respond in ${user_language} language.
        
        For reference and context, here is the chat history:
        {chat_history}

        For totally uncorrelated questions:
            - Respond briefly and guide the patient back to therapy-related topics.
            - Example: "I'm here to assist with your therapy questions. Let's focus on your rehabilitation needs."

        Disclaimer to always include:
            - This is not medical advice. Always consult a doctor before starting any therapy.
    """
)

chain = LLMChain(llm=llm, prompt=prompt)
response = chain.run(input=input, docs=docsFull, patient_id=patient_id, chat_history=chat_history, user_language = user_language)
print(response)