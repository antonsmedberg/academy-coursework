from flask import Flask, request, render_template
from prompt_generator import PromptGenerator

app = Flask(__name__)

@app.route('/')
def index():
    return render_template('index.html')

@app.route('/generate_prompt', methods=['POST'])
def generate_prompt():
    role = request.form['role']
    task = request.form['task']
    specifics = request.form['specifics']
    context = request.form['context']
    examples = request.form['examples']
    notes = request.form['notes']
    deadline = request.form['deadline']
    priority = request.form['priority']
    
    prompt_generator = PromptGenerator()
    prompt = prompt_generator.create_developer_prompt(role, task, specifics, context, examples, notes, deadline, priority)
    
    return render_template('result.html', prompt=prompt)

if __name__ == "__main__":
    app.run(debug=True)

