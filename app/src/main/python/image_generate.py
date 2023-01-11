import openai
import azapi
import requests
import RAKE



stopwords = ['i', 'me', 'my', 'myself', 'we', 'our', 'ours', 'ourselves',
 'you', "you're", "you've", "you'll", "you'd", 'your', 'yours', 'yourself',
  'yourselves', 'he', 'him', 'his', 'himself', 'she', "she's", 'her', 'hers',
   'herself', 'it', "it's", 'its', 'itself', 'they', 'them', 'their', 'theirs',
    'themselves', 'what', 'which', 'who', 'whom', 'this', 'that', "that'll", 'these',
     'those', 'am', 'is', 'are', 'was', 'were', 'be', 'been', 'being', 'have', 'has',
      'had', 'having', 'do', 'does', 'did', 'doing', 'a', 'an', 'the', 'and', 'but', 'if',
       'or', 'because', 'as', 'until', 'while', 'of', 'at', 'by', 'for', 'with', 'about',
        'against', 'between', 'into', 'through', 'during', 'before', 'after', 'above', 'below',
         'to', 'from', 'up', 'down', 'in', 'out', 'on', 'off', 'over', 'under', 'again', 'further',
          'then', 'once', 'here', 'there', 'when', 'where', 'why', 'how', 'all', 'any', 'both', 'each',
           'few', 'more', 'most', 'other', 'some', 'such', 'no', 'nor', 'not', 'only', 'own', 'same',
            'so', 'than', 'too', 'very', 's', 't', 'can', 'will', 'just', 'don', "don't", 'should',
             "should've", 'now', 'd', 'll', 'm', 'o', 're', 've', 'y', 'ain', 'aren', "aren't", 'couldn',
              "couldn't", 'didn', "didn't", 'doesn', "doesn't", 'hadn', "hadn't", 'hasn', "hasn't",
               'haven', "haven't", 'isn', "isn't", 'ma', 'mightn', "mightn't", 'mustn', "mustn't",
                'needn', "needn't", 'shan', "shan't", 'shouldn', "shouldn't", 'wasn', "wasn't",
                 'weren', "weren't", 'won', "won't", 'wouldn', "wouldn't"]
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
    API = azapi.AZlyrics("duckduckgo",accuracy=0.6)
    API.artist = artist_name
    API.title = song_name

    API.getLyrics(ext='lrc')

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
    rake_object = RAKE.Rake(stopwords)

    # Sample text to test RAKE
    text = lyrics

    # Extract keywords
    keywords = rake_object.run(text)
    keywords_one =keyword_selector(text, keywords, 0)
    keywords_two =keyword_selector(text, keywords, 1)
    return str(keywords[0]).split(",")[0].replace("(","").replace("'","")


def scan_song(binary_file):
    data = {
    'api_token': '473250a810b7ccb9cebbdf14d399e915', #valid until 21st of January
    'return': 'apple_music,spotify'
    }
    btfile = bytes(binary_file)

    files = {
    'file': btfile,
    }
    result = requests.post('https://api.audd.io/', data=data, files=files)
    return result.text