import openai
import os






def plot(x,y):
    openai.api_key = "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"
    response = openai.Image.create(
        prompt=str(x) + "" + str(y),
        n=4,
        size="1024x1024"
    )
    image_url = response['data'][0]['url']
    return image_url


