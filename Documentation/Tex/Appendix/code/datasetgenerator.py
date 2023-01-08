
import pandas as pd
from google.colab import drive
drive.mount('/content/drive')
numberofrows=None #100000 
title_basics = pd.read_csv("/content/drive/MyDrive/Dataset/Original/title_basics.tsv",sep='\t',nrows=numberofrows,header=0)
title_principals = pd.read_csv("/content/drive/MyDrive/Dataset/Original/title_principals.tsv",nrows=numberofrows,sep='\t',header=0)

keep_col = ["tconst","titleType","primaryTitle","originalTitle","startYear","runtimeMinutes","genres"]
title_basics = title_basics[keep_col]
title_basics = title_basics[title_basics["titleType"].str.contains("movie") == True]

print(title_basics.head(3))

merged1=pd.merge(title_basics,title_principals,how='inner',on='tconst')
del title_basics,title_principals
print(merged1)

name_basics=pd.read_csv("/content/drive/MyDrive/Dataset/Original/name_basics.tsv",sep='\t',nrows=numberofrows,header=0)

merged2=pd.merge(merged1,name_basics,how='inner',on='nconst')
del merged1
merged2=merged2.drop(columns=["ordering","nconst","birthYear","deathYear","knownForTitles","primaryProfession"])
del name_basics
print(merged2)

category=merged2.groupby('tconst')['category'].apply(list).reset_index(name='category')
job=merged2.groupby('tconst')['job'].apply(list).reset_index(name='job')
characters=merged2.groupby('tconst')['characters'].apply(list).reset_index(name='characters')
primaryName=merged2.groupby('tconst')['primaryName'].apply(list).reset_index(name='primaryName')
result=merged2.drop_duplicates(subset=['tconst'])
result=result.drop(['category'], axis=1).drop(['job'], axis=1).drop(['characters'], axis=1).drop(['primaryName'], axis=1)
result=result.merge(category,on='tconst').merge(job,on='tconst').merge(characters,on='tconst').merge(primaryName,on='tconst')
print(result)

result.to_csv("/content/drive/MyDrive/Dataset/resultSetFinale.csv",index=False)