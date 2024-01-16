import os, sys
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

loader = TextLoader("src/main/resources/scripts/user_manual.txt")
docsFull = loader.load()

text_splitter = RecursiveCharacterTextSplitter(chunk_size=500, chunk_overlap=0)
therapies = text_splitter.split_documents(docsFull)

embeddings = OpenAIEmbeddings(openai_api_key=openai_api_key)

db = FAISS.from_documents(therapies, embeddings)

docs = db.similarity_search(input, k=8)
docs_page_content = " ".join([d.page_content for d in docs])

llm = ChatOpenAI(temperature=0.3, model_name = 'gpt-3.5-turbo', max_tokens=1024, openai_api_key=openai_api_key)

prompt = PromptTemplate(
    input_variables=["patient_id", "input", "docs", "chat_history", "user_language"],
    template="""
        You are a friendly chatbot for Medical Rehabilitation system that helps user to find information about the system and how to use it.

        Patient's name: {patient_id}. 
        Patient's question: {input}.
        Create clear, emphathetic answer by searching for information about user question from the following user manual: {docs}

        It is very important that you respond in {user_language} language.

        For reference and context, here is the history of current conversation:
        {chat_history}


        Only respond to what was asked. Do not add any additional information.
        For totally uncorrelated questions:
            - Respond briefly and guide the patient back to system-related topics.
            - Example: "I'm here to assist with your MedBay experience. Let's focus on that."

    """
)

chain = LLMChain(llm=llm, prompt=prompt)
response = chain.run(input=input, docs=docsFull, patient_id=patient_id, chat_history=chat_history, user_language = user_language)
print(response)