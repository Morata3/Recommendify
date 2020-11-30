from sklearn.neighbors import NearestNeighbors
from fuzzywuzzy import fuzz
import numpy as np
import pandas as pd
import scipy.sparse
from scipy.sparse import csr_matrix
from os.path import dirname, join
import joblib
import json
import joblib



   
import joblib
wide_artist_data_sparse = scipy.sparse.load_npz("lastfm_sparse_artist_matrix.npz")
from sklearn.neighbors import NearestNeighbors

filename = "item_based_collaborative_artist_model.sav"
model_knn = joblib.load(filename)


decode_id_artist = {}
with open('decode_id_artist.json', 'r') as fp:
    decode_id_artist = json.load(fp)




artist = 'Rammstein'
recommendations = []
# Get the id of the song according to the text
# get match
match_tuple = []
for artist_name, idx in decode_id_artist.items():
    ratio = fuzz.ratio(artist_name.lower(), artist.lower())
    if ratio >= 60:
        match_tuple.append((artist_name, idx, ratio))
# sort
match_tuple = sorted(match_tuple, key=lambda x: x[2])[::-1]
if not match_tuple:
    print(f"The recommendation system could not find a match for {song}")

recom_artist_id = match_tuple[0][1]
# Start the recommendation process
print(f"Starting the recommendation process for {artist} ...")
# Return the n neighbors for the song id
distances, indices = model_knn.kneighbors(wide_artist_data_sparse[recom_artist_id], n_neighbors=10+1)
recommendation_ids =sorted(list(zip(indices.squeeze().tolist(), distances.squeeze().tolist())), key=lambda x: x[1])[:0:-1]


# return the name of the song using a mapping dictionary
recommendations_map = {artist_id: artist_name for artist_name, artist_id in decode_id_artist.items()}
# Translate this recommendations into the ranking of song titles recommended
for i, (idx, dist) in enumerate(recommendation_ids):
    recommendations.append(recommendations_map[idx])


print(recommendations)