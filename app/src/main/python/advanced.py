# -*- coding: utf-8 -*-
"""
Created on Sat Nov 21 13:38:44 2020

@author: Pablo
"""


import pandas as pd
# import numpy as np
# from sklearn.neighbors import NearestNeighbors

bailable=1
lowenergy=1
highenergy=0
positive=0
negative=1
instrumental=1
directo=0


lim_dance = 0.75
lim_liveness = 0.80
lim_instru = 0.98
lim_lowenergy = 0.33
lim_highenergy = 0.75
lim_positive = 0.80
lim_negative = 0.30
lim_speechiness = 0.01


music = pd.read_csv('spotify1.csv')
music = music.drop(music[music['Song Popularity'] < 20].index)
music_aux = music
music = music.reset_index(drop=True)

## Canciones bailables, con energía alta,media baja, alegres, tristes en directo

def filter_songs(music):
        
    if bailable == 1: #canciones con danceability alta
        music = music.drop(music[music.danceability < lim_dance].index)
        music = music.reset_index(drop=True) 
        
    if lowenergy == 1: ## 0 -> deja solo canciones con poca energia
        music = music.drop(music[music.energy > lim_lowenergy].index)
        music = music.reset_index(drop=True)
    elif highenergy == 1: ## 2 -> deja solo canciones con mucha energia
        music = music.drop(music[music.energy < lim_highenergy].index)
        music = music.reset_index(drop=True)
        
    if positive == 1: #canciones positivas
        music = music.drop(music[music.valence < lim_positive].index)
        music = music.reset_index(drop=True)
    elif positive == 1: #canciones tristes
        music = music.drop(music[music.valence > lim_positive].index)
        music = music.reset_index(drop=True)
    
    if directo == 1: #canciones en directo
        music = music.drop(music[music.liveness < lim_liveness].index)
        music = music.reset_index(drop=True)
    
    if instrumental == 1: #puro instrumento
        music = music.drop(music[music.instrumentalness < lim_instru].index)
        music = music.drop(music[music.speechiness > lim_speechiness].index)
        music = music.reset_index(drop=True)

        
    return music

music = filter_songs(music_aux)

while music['Song Name'].count() <= 15:
    lim_dance = lim_dance - 0.05
    lim_liveness = lim_liveness - 0.03
    lim_instru = lim_instru - 0.05
    lim_lowenergy = lim_lowenergy + 0.05
    lim_highenergy = lim_highenergy - 0.05
    lim_positive = lim_positive - 0.05
    lim_negative = lim_negative + 0.05
    lim_speechiness = lim_speechiness + 0.01
    music = filter_songs(music_aux)

    
    
    
    
    
    
    
    
    
    
    




    
    