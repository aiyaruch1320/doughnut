/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { ImageWithMask } from './ImageWithMask';
import type { MultipleChoicesQuestion } from './MultipleChoicesQuestion';
import type { QuizQuestion } from './QuizQuestion';
export type QuizQuestionAndAnswer = {
    id: number;
    multipleChoicesQuestion: MultipleChoicesQuestion;
    imageWithMask?: ImageWithMask;
    quizQuestion: QuizQuestion;
};
