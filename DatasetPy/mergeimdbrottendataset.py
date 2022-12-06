# -*- coding: utf-8 -*-
"""MergeImdbRottenDataset.ipynb

Automatically generated by Colaboratory.

Original file is located at
    https://colab.research.google.com/drive/1ANg6GlUwdmqVy__ISHVKurWoWvFjYqFF
"""

import pandas as pd
from google.colab import drive
drive.mount('/content/drive')
numberofrows=None 
imdb = pd.read_csv("/content/drive/MyDrive/Dataset/resultSetFinale.csv",nrows=numberofrows,header=0)
rotten = pd.read_csv("/content/drive/MyDrive/Dataset/resultSetRotten.csv",nrows=numberofrows,header=0)

merged = {}
choose = ['primaryTitle', 'originalTitle']
for x in choose:
  merged[x]=pd.merge(imdb,rotten,how='inner', left_on=x, right_on='movie_title')
  print(x)
  merged[x]=merged[x].drop_duplicates(subset=[x])
  merged[x]=merged[x].drop(columns=['rotten_tomatoes_link', 'movie_title']+[j for j in choose if j!=x])
  print(len(pd.unique(merged[x][x])))
  print(list(merged[x]))
  print("=====================")
  merged[x].to_csv(f"/content/drive/MyDrive/Dataset/ImdbJoinRotten{x}.csv",index=False)