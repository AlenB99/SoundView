import openai
import azapi
import RAKE
import nltk
from nltk.corpus import stopwords


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

    return API.lyrics.replace("\n"," ")




def keyword_selector(text, keywords, i):
    if len(keywords[i][0].split()) > 1:
        text_new = text
        text_new+=" "
        text_new+= keywords[i][0] 
        if len(text_new.split()) >= 4:
            return text_new
        
        else:
            keyword_selector(text_new, keywords, i+1) 

def nlp_on_lyrics(lyrics):
    stop_dir = stopwords.words('english')
    rake_object = RAKE.Rake(stop_dir)

    # Sample text to test RAKE
    text = 

    # Extract keywords
    keywords = rake_object.run(text)
    print ("keywords: ", keywords)
    keywords_one =keyword_selector(text, keywords, 0)
    keywords_two =keyword_selector(text, keywords, 1)
    return keywords_one