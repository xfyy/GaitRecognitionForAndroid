
# coding: utf-8

# In[ ]:



from flask import Flask,render_template,jsonify,json,request
#from fabric.api import *

import os, os.path
#import cv2
import glob
import numpy as np
from sklearn.model_selection import train_test_split
from sklearn.linear_model import LogisticRegression
from sklearn.ensemble import RandomForestClassifier, RandomForestRegressor
from sklearn import metrics
from sklearn.neighbors import KNeighborsClassifier
from sklearn.neural_network import MLPClassifier
from sklearn.model_selection import train_test_split
from sklearn import svm
from sklearn.metrics import accuracy_score

application = Flask(__name__)

# In[20]:


#data fetching
features = []
labels = []
fileObject=open("completeData.txt","r");
data=fileObject.read();
data_split=data.split("\n");
for x in data_split[:33460]:
    record_split= x.split(",");
    triplet=[];
    triplet.append( float(record_split[0]) );
    triplet.append( float(record_split[1]) );
    triplet.append( float(record_split[2]) );
    features.append(triplet)
    labels.append(record_split[3]);


# In[21]:


#splitting data

X_train, X_test, y_train, y_test = train_test_split(features, labels, test_size=0.05, random_state=42)
#print "X_train.shape()" ,X_train.shape()


# In[ ]:


model = RandomForestClassifier(n_estimators = 200, oob_score = True, n_jobs = -1,random_state =50, max_features = "auto", min_samples_leaf = 200) #0.637045537341

#model1=KNeighborsClassifier();
model1=MLPClassifier();
print "len(X_train)",len(X_train)
np_X_train = np.array(X_train)
np_y_train = np.array(y_train)

from sklearn.model_selection import KFold # import KFold
kf = KFold(n_splits=5) # Define the split - into 2 folds 
tmp_df = X_train
print "model traning"

for train_index, test_index in kf.split(tmp_df):
    np_train_index = np.array(train_index)
    print("np_train_index len ", len(np_train_index))
    X_train_cv = np_X_train[np_train_index]
    X_test_cv = np_y_train[np_train_index]
#    model.fit(X_train_cv, X_test_cv)
    model1.fit(X_train_cv, X_test_cv)


#output=model.predict( np.array( X_test ) )
#print(accuracy_score(y_test, output))

output=model1.predict( np.array( X_test ) )
print(accuracy_score(y_test, output))




@application.route("/classify",methods=['post'])
def classify() :
    x_cor=float("{0:.4f}".format( request.json["x"] ) )
    y_cor=float("{0:.4f}".format( request.json["y"] ) )
    z_cor=float("{0:.4f}".format( request.json["z"] ) )
    data=[ x_cor , y_cor ,z_cor  ]
    global model;
    print model.predict(np.array( data ))
    return jsonify({"x" : "rajesh"});

if __name__ == "__main__":
    application.run(host='0.0.0.0')
