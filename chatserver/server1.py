import socket
###############################KNN################################################################################
import pandas as pd
from sklearn.model_selection import train_test_split
from sklearn.metrics import accuracy_score
from sklearn.neighbors import KNeighborsClassifier
import io 

data  = pd.read_csv("D:/Master ALX/PFEAI/New dataset/dataset.csv")
data_sevrity = pd.read_csv("D:/Master ALX/PFEAI/New dataset/Symptom-severity.csv")

#convert data_severity to dictionnary
data_dict = data_sevrity.set_index('Symptom').T.to_dict()

def remove_space_between_word(dataset):
    for col in dataset.columns:
        for i in range(len(dataset[col])):
            if (type(dataset[col][i]) == str ):
                dataset[col][i] = dataset[col][i].strip()
                dataset[col][i] = dataset[col][i].replace(" ", "_")
    return dataset

new_df = remove_space_between_word(data)

def encode_data(dataset , data_dict_weigth):
    cols = dataset.columns
    for columnName in cols:
        for i in range(len(dataset[columnName])):
            try:
                dataset[columnName][i] = data_dict_weigth[dataset[columnName][i]]["weight"]
            except:
                pass
    dataset = dataset.fillna(0) # put empty cell to 0
    dataset = dataset.replace("foul_smell_of_urine" , 5)
    dataset = dataset.replace("dischromic__patches" , 6)
    dataset = dataset.replace("spotting__urination" , 6)
    return dataset

df = encode_data(new_df , data_dict)

#check if all Symptoms are replace by their weigth
names = []
for col in df.columns:
    if(col != "Disease"):
        for i in range(len(df[col])):
            if (type(df[col][i]) == str ):
                if df[col][i] not in names :
                    names.append(df[col][i])
                    
df_data = df.drop('Disease' , axis =1)
label = data["Disease"]

x_train, x_test, y_train, y_test = train_test_split(df_data, label, shuffle=True, train_size = 0.70)

knn = KNeighborsClassifier(n_neighbors=3)
knn.fit(x_train, y_train)
y_pred = knn.predict(x_test)
acc = accuracy_score(y_test, y_pred)

# Create a list of symptoms
symptoms = ["muscle wasting", "patches in throat", "high fever", "extra marital contacts", "watering from eyes", "dizziness", "breathlessness"]

def detect_desease(symptom):
    # Create a Pandas DataFrame from the list of symptoms
    df1=pd.DataFrame()
    df1=df_data[0:0]
    if len(symptom) < 17:
        i=len(symptom)
        while i<17:
            symptom.append(' ')
            i+=1
    df1.loc[len(df1)]=symptom

    new_df1 = remove_space_between_word(df1)
    df2 = encode_data(new_df1 , data_dict)
    df2.replace('', 0, inplace=True)
    #print(df2)
    pred = knn.predict(df2)
    return pred

#print(detect_desease(symptoms))


###################################################################################################################
# Create a socket object
server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

# Get local machine name
host = socket.gethostname()

# Bind the socket to a public host and a well-known port
server_socket.bind((host, 8000))

# Listen for incoming connections
server_socket.listen(1)

while True:
    # Wait for a client to connect
    client_socket, addr = server_socket.accept()

    # Receive the data sent by the client
    question = client_socket.recv(1024)

    # Process the question and send back an answer
    answer = "This is the answer to your question: " + detect_desease(symptoms)
    client_socket.send(answer.encode())

    # Close the connection with the client
    client_socket.close()