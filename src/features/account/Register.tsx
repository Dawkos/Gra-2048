import Avatar from '@mui/material/Avatar';
import TextField from '@mui/material/TextField';
import Grid from '@mui/material/Grid';
import Box from '@mui/material/Box';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';
import { Paper } from '@mui/material';
import { Link, useHistory } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import { LoadingButton } from '@mui/lab';
import agent from '../../app/api/agent';
import { toast } from 'react-toastify';

export default function Register() {
    const history = useHistory();
    const { register, handleSubmit, setError, formState: { isSubmitting, errors, isValid } } = useForm({
        mode: 'all'
    });

    function handleApiErrors(errors: any) {
        if (errors) {
            errors.forEach((error: string) => {
                if (error.includes('Password')) {
                    setError('password', { message: error })
                } else if (error.includes('Email')) {
                    setError('email', { message: error })
                } else if (error.includes('Username')) {
                    setError('username', { message: error })
                }
            });
        }
    }

    return (
        <Container component={Paper} maxWidth="sm" sx={{ display: 'flex', flexDirection: 'column', alignItems: 'center', p: 4 }}>
            <Avatar sx={{ m: 1, bgcolor: 'secondary.main' }}>
                <LockOutlinedIcon />
            </Avatar>
            <Typography component="h1" variant="h5">
                Register
            </Typography>
            <Box component="form"
                onSubmit={handleSubmit((data) =>
                    agent.Account.register(data)
                        .then(() => {
                            toast.success('Rejestracja przebiegła pomyślnie, możesz się zalogować');
                            history.push('/login');
                        })
                        .catch(error => handleApiErrors(error))
                )}
                noValidate sx={{ mt: 1 }}
            >
                <TextField
                    margin="normal"
                    fullWidth
                    label="Login"
                    autoFocus
                    {...register('username', { required: 'Wymagany login' })}
                    error={!!errors.username}
                    helperText={errors?.username?.message}
                />
                <TextField
                    margin="normal"
                    fullWidth
                    label="Adres e-mail "
                    {...register('email', { 
                        required: 'Wymagany e-mail ',
                        pattern: {
                            value: /^\w+[\w-.]*@\w+((-\w+)|(\w*)).[a-z]{2,3}$/,
                            message: 'Adres e-mail nie jest poprawny'
                        } 
                    })}
                    error={!!errors.email}
                    helperText={errors?.email?.message}
                />
                <TextField
                    margin="normal"
                    fullWidth
                    label="Hasło"
                    type="password"
                    {...register('password', { 
                        required: 'Wymagane hasło',
                        pattern: {
                            value: /(?=^.{6,10}$)(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&amp;*()_+}{&quot;:;'?/&gt;.&lt;,])(?!.*\s).*$/,
                            message: 'Hasło jest niekompletne'
                        }
                    })}
                    error={!!errors.password}
                    helperText={errors?.password?.message}
                />
                <LoadingButton
                    disabled={!isValid}
                    loading={isSubmitting}
                    type="submit"
                    fullWidth
                    variant="contained"
                    sx={{ mt: 3, mb: 2 }}
                >
                    Zarejestruj się
                </LoadingButton>
                <Grid container>
                    <Grid item>
                        <Link to='/login'>
                            {"Masz już konto? Zaloguj się"}
                        </Link>
                    </Grid>
                </Grid>
            </Box>
        </Container>
    );
}