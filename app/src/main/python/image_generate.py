import openai
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


