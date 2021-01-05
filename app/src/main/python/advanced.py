# -*- coding: utf-8 -*-
"""
Created on Sat Nov 21 13:38:44 2020

@author: Pablo
"""


import pandas as pd
from os.path import dirname,join

# import numpy as np
# from sklearn.neighbors import NearestNeighbors
lim_dance = 0.7
lim_liveness = 0.65
lim_instru = 0.80
lim_lowenergy = 0.50
lim_highenergy = 0.60
lim_positive = 0.60
lim_negative = 0.50
lim_speechiness = 0.2
lim_loudness = 0.55
lim_quiet = 0.45

SpotifyFile = join(dirname(__file__),"spotify2.csv")
music = pd.read_csv(SpotifyFile)
music = music.drop(music[music['Song Popularity'] < 20].index)
music_aux = music
music = music.reset_index(drop=True)

## Canciones bailables, con energía alta,media baja, alegres, tristes en directo

def filter_songs(directo,bailable,positive,negative,lowenergy,highenergy,instrumental,loudness,quiet):

    #print(directo,bailable,positive,negative,lowenergy,highenergy,instrumental)
    music3 = music_aux
    if bailable == 1: #canciones con danceability alta
        music3 = music3.drop(music3[music3.danceability < lim_dance].index)
        music3 = music3.reset_index(drop=True) 
        
    if lowenergy == 1: ## 0 -> deja solo canciones con poca energia
        music3 = music3.drop(music3[music3.energy > lim_lowenergy].index)
        music3 = music3.reset_index(drop=True)
    if highenergy == 1: ## 2 -> deja solo canciones con mucha energia
        music3 = music3.drop(music3[music3.energy < lim_highenergy].index)
        music3 = music3.reset_index(drop=True)
        
    if positive == 1: #canciones positivas
        music3 = music3.drop(music3[music3.valence < lim_positive].index)
        music3 = music3.reset_index(drop=True)
    if negative == 1: #canciones tristes
        music3 = music3.drop(music3[music3.valence > negative].index)
        music3 = music3.reset_index(drop=True)
        
    if loudness == 1: #canciones loud
        music3 = music3.drop(music3[music3.loudness < lim_loudness].index)
        music3 = music3.reset_index(drop=True)
    if quiet == 1: #canciones suaves
        music3 = music3.drop(music3[music3.loudness > lim_quiet].index)
        music3 = music3.reset_index(drop=True)
        
    
    if directo == 1: #canciones en directo
        music3 = music3.drop(music3[music3.liveness < lim_liveness].index)
        music3 = music3.reset_index(drop=True)
    
    if instrumental == 1: #puro instrumento
        music3 = music3.drop(music3[music3.instrumentalness < lim_instru].index)
        music3 = music3.drop(music3[music3.speechiness > lim_speechiness].index)
        music  = music3.reset_index(drop=True)


    music3 = music3.drop_duplicates(subset=['Song Name'], keep='first')
    music3 = music3.sort_values(by= 'Song Popularity', ascending = False)
    music3 = music3.head(10)[['Song Name','id','Artist']].values.tolist()
    music3 = [dict(zip(['song_name','id','artist'], l)) for l in music3]



    return music3

music = filter_songs(0,0,1,0,0,0,0,1,0)
#directo,bailable,positive,negative,lowenergy,highenergy,instrumental,loudness,quiet
print(music)
'''while music['Song Name'].count() <= 15:
    lim_dance = lim_dance - 0.05
    lim_liveness = lim_liveness - 0.03
    lim_instru = lim_instru - 0.05
    lim_lowenergy = lim_lowenergy + 0.05
    lim_highenergy = lim_highenergy - 0.05
    lim_positive = lim_positive - 0.05
    lim_negative = lim_negative + 0.05
    lim_speechiness = lim_speechiness + 0.01
    music = filter_songs(music_aux) '''
