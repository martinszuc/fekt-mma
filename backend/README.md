# Notes REST API (cv6 backend)

Django + Django REST Framework backend for the Flutter Notes app.

## Setup

```bash
cd backend
python3 -m venv env
source env/bin/activate
pip install -r requirements.txt
python manage.py migrate
```

## Run

```bash
source env/bin/activate
python manage.py runserver
```

Server: http://127.0.0.1:8000/

## Endpoints

| Method | URL | Description |
|--------|-----|-------------|
| GET | `/` | List API routes |
| GET | `/notes/` | List all notes |
| POST | `/notes/` | Create note |
| GET | `/notes/<id>/` | Get single note |
| PUT | `/notes/<id>/` | Update note |
| DELETE | `/notes/<id>/` | Delete note |

## Admin

```bash
python manage.py createsuperuser
```

Then visit http://127.0.0.1:8000/admin/
