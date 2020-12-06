# -*- coding: utf-8 -*-
"""
Created on Wed Nov 25 11:53:38 2020

@author: Pablo
"""

# -*- coding: utf-8 -*-
"""
Created on Tue Nov 24 19:26:18 2020

@author: Pablo
"""

import numpy as np 
import pandas as pd
from sklearn.metrics.pairwise import cosine_similarity
from os.path import dirname,join

SpotifyFile = join(dirname(__file__),"spotify2.csv")
data = pd.read_csv(SpotifyFile)

def rank_song_similarity_by_measure(song, artist, genre_parameter):
    
    song_and_artist_data = data[(data.Artist == artist) & (data['Song Name'] == song)].sort_values('Year')[0:1]
    
    print(song_and_artist_data)
    
    similarity_data = data.copy()
    
    data_values = similarity_data.loc[:,['acousticness', 'danceability',
       'energy', 'instrumentalness', 'key', 'liveness', 'loudness', 'mode',
       'speechiness', 'tempo', 'valence']]
    
    similarity_data['Similarity with song'] =cosine_similarity(data_values, data_values.to_numpy()[song_and_artist_data.index[0],None]).squeeze()
    
    artist_genres = set(*song_and_artist_data.Genres)

    similarity_data.Genres = similarity_data.Genres.apply(lambda Genres: list(set(Genres).intersection(artist_genres)))
    
    similarity_lengths = similarity_data.Genres.str.len()
    similarity_data = similarity_data.reindex(similarity_lengths[similarity_lengths >= genre_parameter].sort_values(ascending=False).index)
    
    similarity_data = similarity_data[similarity_data['Song Decade'] == song_and_artist_data['Song Decade'].values[0]]
 
    similarity_data.rename(columns={'Song Name': f'Similar Song to {song}'}, inplace=True)
    
    similarity_data = similarity_data.sort_values(by= 'Similarity with song', ascending = False)
    
    similarity_data = similarity_data[['Artist', f'Similar Song to {song}',
       'Song Popularity', 'Year', 'Genres', 'Artist Popularity', 'Song Decade', 'Similarity with song',
       'acousticness', 'danceability', 'energy', 'instrumentalness', 'key',
       'liveness', 'loudness', 'mode', 'speechiness', 'tempo', 'valence']]
    
    similarity_data = similarity_data.drop_duplicates(subset=[f'Similar Song to {song}'], keep='first')
    
    lista = similarity_data.head(15)[f'Similar Song to {song}'].tolist()

    
    return lista

recomendaciones = rank_song_similarity_by_measure('Bohemian Rhapsody', 'Queen',2)
print(recomendaciones)