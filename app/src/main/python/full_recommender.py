from sklearn.neighbors import NearestNeighbors
from fuzzywuzzy import fuzz
import numpy as np
import pandas as pd
import scipy.sparse
from scipy.sparse import csr_matrix
import joblib
import json
from os.path import dirname, join




def recommend():   
    # obtain a sparse matrix
    mat_songs_features_filename= join(dirname(__file__), "df_mat_songs_features.npz")
    mat_songs_features = scipy.sparse.load_npz(mat_songs_features_filename)
    decode_id_song = {}
    decode_id_song_filename= join(dirname(__file__), 'decode_id_songs.json')
    with open(decode_id_song_filename, 'r') as fp:
        decode_id_song = json.load(fp)
    song= "Iconography"

    #model = NearestNeighbors(metric='cosine', algorithm='brute', n_neighbors=20, n_jobs=-1)
    #model.fit(mat_songs_features)
    model_filename = join(dirname(__file__), 'user_based_collaborative.sav')
    model = joblib.load(model_filename)
    # load the model from disk
    


    recommendations = []
    # Get the id of the song according to the text
    # get match
    match_tuple = []
    for title, idx in decode_id_song.items():
        ratio = fuzz.ratio(title.lower(), song.lower())
        if ratio >= 60:
            match_tuple.append((title, idx, ratio))
    # sort
    match_tuple = sorted(match_tuple, key=lambda x: x[2])[::-1]
    if not match_tuple:
        print(f"The recommendation system could not find a match for {song}")

    recom_song_id = match_tuple[0][1]
    # Start the recommendation process
    print(f"Starting the recommendation process for {song} ...")
    # Return the n neighbors for the song id
    distances, indices = model.kneighbors(mat_songs_features[recom_song_id], n_neighbors=10+1)
    recommendation_ids =sorted(list(zip(indices.squeeze().tolist(), distances.squeeze().tolist())), key=lambda x: x[1])[:0:-1]


    # return the name of the song using a mapping dictionary
    recommendations_map = {song_id: song_title for song_title, song_id in decode_id_song.items()}
    # Translate this recommendations into the ranking of song titles recommended
    for i, (idx, dist) in enumerate(recommendation_ids):
        recommendations.append(recommendations_map[idx])


    print(recommendations)





    #new_recommendations = model.make_recommendation(new_song=song, n_recommendations=10)

    return ', '.join(recommendations)
