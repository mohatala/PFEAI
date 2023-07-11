import numpy as np
import pandas as pd
import nltk
from nltk.tokenize import word_tokenize
from nltk.corpus import stopwords
import spacy
import re
from sklearn.feature_extraction.text import TfidfVectorizer,CountVectorizer
from sklearn.model_selection import train_test_split
from sklearn.neighbors import KNeighborsClassifier
from sklearn import metrics
from sklearn.metrics import classification_report
from sklearn.preprocessing import StandardScaler
import matplotlib.pyplot as plt
nlp = spacy.load("en_core_web_sm")
nltk.download('stopwords')

df = pd.read_csv('C:/Users/21260/Desktop/PFEAI/Test20.csv',sep=';')

#print(df.sample(6))

def delete_punctuation(text):
    text = re.sub(r'[^\w\s]', '', text)  
    text = re.sub(r'\d+', '', text) 
    text=re.sub(" \d+", " ", text)    
    text = text.strip()                 
    return text

df['symptom1'] = df['symptom1'].apply(delete_punctuation)

stop_words = set(stopwords.words('english'))

def tokenizer(text):
    tokens = word_tokenize(text.lower())   
    tokens = [t for t in tokens if t not in stop_words]  
    token=' '.join(tokens)
    return token

df['symptom1'] = df['symptom1'].apply(tokenizer)

def vectorizer_text(list):
    vectorizer = TfidfVectorizer(analyzer='word',stop_words = 'english')

    tfidf = vectorizer.fit_transform(list)

    tfidf_df = pd.DataFrame(tfidf.toarray(), columns=vectorizer.get_feature_names_out())

    return tfidf_df

def count_vectorizer_text(list):
    countvectorizer = CountVectorizer(analyzer= 'word', stop_words='english')
    count_wm = countvectorizer.fit_transform(list)
    count_tokens = countvectorizer.get_feature_names_out()
    df_countvect = pd.DataFrame(data = count_wm.toarray(),columns = count_tokens)
    return df_countvect


df = pd.concat([df, vectorizer_text(df['symptom1'])], axis=1)
df.drop('Code', axis=1, inplace=True)
df.drop('symptom1', axis=1, inplace=True)
#df.drop('key', axis=1, inplace=True)

print(df.head())

X = df.loc[:, df.columns != "name"]
y = df.name
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.3, random_state=2)

"""scaler = StandardScaler()
scaler.fit(X_train)
X_train = scaler.transform(X_train)
X_test = scaler.transform(X_test)"""

Range_k = range(1,8)
scores = {}
scores_list = []
for k in Range_k:
   classifier = KNeighborsClassifier(n_neighbors=k)
   classifier.fit(X_train, y_train)
   y_pred = classifier.predict(X_test)
   scores[k] = metrics.accuracy_score(y_test,y_pred)
   scores_list.append(metrics.accuracy_score(y_test,y_pred))
result = metrics.confusion_matrix(y_test, y_pred)
print("Confusion Matrix:")
print(result)
result1 = metrics.classification_report(y_test, y_pred,zero_division=0)
print("Classification Report:",)
print (result1)

plt.plot(Range_k,scores_list)
plt.xlabel("Value of K")
plt.ylabel("Accuracy")
plt.show()
#knn
"""knn = KNeighborsClassifier(n_neighbors=3)
knn.fit(X_train, y_train)
y_pred = knn.predict(X_test)
#print(y_pred)
acc = metrics.accuracy_score(y_test, y_pred)
print("kNN model accuracy:",acc)
print(classification_report(y_test, y_pred))"""

#test="Skin lesion Leg pain Foot or  Poor circulation Lymphedema Leg cramps or Loss of sensation Leg pain Peripheral edema Weakness toe pain Vomiting Ache all over Headache Hoarse voice"
test="Facial pain Wrist pain Fluid retention Jaw swelling Open wound of the cheek"
test=tokenizer(test)
test=vectorizer_text([test])
#test=X_test.sample(1)
df1=pd.DataFrame()
df1=X[0:0]
df2=pd.concat([df1,test], axis=0)
df2=df2.replace(np.nan,0)
print(df2.head())
pred = classifier.predict(df2)
print(pred)

