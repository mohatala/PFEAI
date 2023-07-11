import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
from sklearn.model_selection import train_test_split
from sklearn.metrics import f1_score, accuracy_score, confusion_matrix ,classification_report
from sklearn.ensemble import RandomForestClassifier
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
#print(new_df.head())

def enc(dataset):
    for ind in data_sevrity.index:
        dataset = dataset.replace(data_sevrity["Symptom"][ind] , data_sevrity["weight"][ind])
    dataset = dataset.fillna(0) # put empty cell to 0
    dataset = dataset.replace("foul_smell_of_urine" , 5)
    dataset = dataset.replace("dischromic__patches" , 6)
    dataset = dataset.replace("spotting__urination" , 6)
    return dataset

ds=enc(data)

def encode_data(dataset , data_dict_weigth):
    cols = dataset.columns
    for columnName in cols:
        for i in range(len(dataset[columnName])):
            try:
            #print(data_dict[data2[columnName][i]]["weight"])
                dataset[columnName][i] = data_dict_weigth[dataset[columnName][i]]["weight"]
            except:
                pass
    dataset = dataset.fillna(0) # put empty cell to 0
    dataset = dataset.replace("foul_smell_of_urine" , 5)
    dataset = dataset.replace("dischromic__patches" , 6)
    dataset = dataset.replace("spotting__urination" , 6)
    return dataset

df = encode_data(new_df , data_dict)
#print(df.head())

#check if all Symptoms are replace by their weigth
names = []
for col in df.columns:
    if(col != "Disease"):
        for i in range(len(df[col])):
            if (type(df[col][i]) == str ):
                if df[col][i] not in names :
                    names.append(df[col][i])
                    
#print(" no replace are :" , names)  

df_data = df.drop('Disease' , axis =1)
label = data["Disease"]

x_train, x_test, y_train, y_test = train_test_split(df_data, label, shuffle=True, train_size = 0.70)
"""randomFC = RandomForestClassifier()
randomFC.fit(x_train, y_train)
result = randomFC.predict(x_test)
print(randomFC)"""

knn = KNeighborsClassifier(n_neighbors=3)
knn.fit(x_train, y_train)
y_pred = knn.predict(x_test)
#print(y_pred)
acc = accuracy_score(y_test, y_pred)
print("kNN model accuracy:",acc)

#print(classification_report(y_true=y_test, y_pred=y_pred))
#print('F1-score% =', f1_score(y_test, y_pred, average='macro')*100, '|', 'Accuracy% =', accuracy_score(y_test, y_pred)*100)


test  = pd.read_csv("D:/Master ALX/PFEAI/New dataset/test_dataset.csv")
# Create a list of symptoms
s="muscle wasting, patches in throat, high fever, extra marital contacts, watering from eyes, dizziness, breathlessness"
symptoms = ["muscle wasting", "patches in throat", "high fever", "extra marital contacts", "watering from eyes", "dizziness", "breathlessness"]
sy=s.split(',')
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

res=detect_desease(sy).tolist()
print(res[0])

