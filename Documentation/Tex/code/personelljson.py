
import pandas as pd
from ast import literal_eval
from google.colab import drive
drive.mount('/content/drive')

numberofrows=None
df = pd.read_csv("/content/drive/MyDrive/Dataset/ImdbJoinRottenprimaryTitle.csv",nrows=numberofrows,header=0)

#print([x.split(',') for x in df['genres']])
print(df)

col = ["primaryName","category","job","characters"]
col1 = ["critic_name","top_critic","review_type","review_score","review_date", "review_content"]

df['personnel'] = ""
df['review'] = ""

for row in range(df[col[0]].size):
  it = df['genres'][row]
  df['genres'][row] = ['"' + x + '"' for x in it.split(',')] if it != '\\N' else []
  tmp = []
  for c in col:
    tmp.append({c:eval(df[c][row])})
  res = []
  for c in range(len(tmp[0][col[0]])):
    res.append({})
  for i, j in zip(col, tmp):
    for idx, x in enumerate(j[i]):
      #print(i, idx, x)
      if x != '\\N':
        if i == 'characters':
          x = eval(x)
        res[idx]["'" + i + "'"] = "'" + str(x).replace("'", "##single-quote##").replace('"', "##double-quote##") + "'"
  df['personnel'][row] = list(res)
  #print(res)
  ###
  tmp = []
  for c in col1:
    to_eval = df[c][row].replace('nan', 'None')
    arr = eval(to_eval)
    if c == "review_date":
      for i, elem in enumerate(arr):
        arr[i] = elem + "T00:00:00.000+00:00"
    tmp.append({c:arr})
    #print(tmp)
  res = []
  for c in range(len(tmp[0][col1[0]])):
    res.append({})
  for i, j in zip(col1, tmp):
    for idx, x in enumerate(j[i]):
      #print(i, idx, x)
      if x != '\\N':
        res[idx]["'" + i + "'"] = "'" + str(x).replace("True", "true").replace("False", "false").replace("'", "##single-quote##").replace('"', "##double-quote##") + "'"
  df['review'][row] = list(res)
  #df['review'][row] = eval(str(res))
  #print(res)
  #print()

df=df.drop(columns=col)
df=df.drop(columns=col1)
df=df.drop(columns=['tconst'])

print(df["review"][0])

it = df['personnel'][0]#[4]['review_content']
print(type(it))
print(it)

df.to_csv("/content/drive/MyDrive/Dataset/movieCollectionEmbeddedReviewPersonnel.csv",index=False)
df = df.head(20)
df.to_csv("/content/drive/MyDrive/Dataset/headmovieCollectionEmbeddedReviewPersonnel.csv",index=False)