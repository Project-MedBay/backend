import os, sys, json
from dotenv import load_dotenv
import openai
from langchain.llms.openai import OpenAI
from langchain.prompts import PromptTemplate
from langchain.embeddings.openai import OpenAIEmbeddings
from langchain.vectorstores import FAISS
from langchain.chains import RetrievalQA

load_dotenv()
openai_api_key = os.getenv("OPENAI_API_KEY")

llm = OpenAI(temperature=0.3, model_name = 'text-davinci-003', max_tokens=1024)

patient_id = sys.argv[1]
symptoms = sys.argv[2]

file_path = 'therapy_type.json'



# Debugging: Check if the file exists and is not empty
if os.path.exists(file_path) and os.path.getsize(file_path) > 0:
    with open(file_path, 'r') as file:
        # Debugging: Print the first few lines to check file content
        for _ in range(5):
            print(file.readline())
        file.seek(0)  # Reset file pointer to the start

        therapies = json.load(file)
else:
    print(f"Error: File '{file_path}' not found or is empty.")

embeddings = OpenAIEmbeddings(openai_api_key=openai_api_key)

jsonsearch = FAISS.from_documents(therapies, embeddings)

qa = RetrievalQA.from_chain_type(llm=llm, chain_type="stuff", retriever=jsonsearch.as_retriever())

prompt = PromptTemplate(
    input_variables=["patient_id", "symptoms"],
    template="""
        Patient's name: {patient_id}. 
        Patient's question: {symptoms}.

        Action:
            - Analyze symptoms to identify suitable therapy.
            - Provide an overview of the recommended therapy. Say as much as possible about it, including the benefits and risks.
            - You can also provide information about the therapy's duration, cost, and availability.
            - You can give info about therapy not only from given vector but also from your own knowledge.

        Guidelines:
            - Use clear, empathetic language.
            - Tailor the recommendation to the patient's symptoms.
            - Emphasize safety and the importance of following the therapy plan.

        For non-medical rehabilitation queries:
            - Respond briefly and guide the patient back to therapy-related topics.
            - Example: "I'm here to assist with your therapy questions. Let's focus on your rehabilitation needs."

        Disclaimer to always include:
            - This is not medical advice. Always consult a doctor before starting any therapy.

        Note: Keep the response focused on therapy guidance, avoiding technical jargon and emphasizing the need for professional medical consultation.
    """
)

final_prompt = prompt.format(
    patient_id=patient_id,
    symptoms=symptoms
)

qa.run(final_prompt)