import openai
import azapi
import os


def image_generate(x):
    openai.api_key = "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"
    response = openai.Image.create(
        prompt=str(x),
        n=4,
        size="1024x1024"
    )
    image_url1 = response['data'][0]['url']
    image_url2 = response['data'][1]['url']
    image_url3 = response['data'][2]['url']
    image_url4 = response['data'][3]['url']
    image_urls= [image_url1,image_url2,image_url3,image_url4]
    return image_urls


def get_song_lyrics(x):
    artist_name= x.split("-")[0]
    song_name= x.split("-")[1]
    API = azapi.AZlyrics(accuracy=0.5)
    API.artist = artist_name
    API.title = song_name

    API.getLyrics(save=True, ext='lrc')

    #print(API.lyrics)
    # Correct Artist and Title are updated from webpage
    #print(API.title, API.artist)

    #returns lyrics as string
    return API.lyrics