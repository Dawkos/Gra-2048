import * as yup from 'yup';

export const validationSchema = [
    yup.object({
        fullName: yup.string().required('Podaj imię i nazwisko'),
        address1: yup.string().required('Podaj ulice'),
        address2: yup.string().required('Podaj numer budynku/mieszkania'),
        city: yup.string().required('Podaj miasto'),
        zip: yup.string().required('Podaj kod pocztowy'),
        country: yup.string().required('Podaj kraj'),
    }),
    yup.object(),
    yup.object({
        nameOnCard: yup.string().required()
    })
] 