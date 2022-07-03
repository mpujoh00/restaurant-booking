import {authAxios} from '@/http-common'

class CourseService {

    createCourse(data){
        return authAxios.post('/courses', data, {
            headers: {
                Authorization: "Bearer " + localStorage.getItem('token')
            }
        })
    }

    getCoursesByRestaurant(restaurantId, courseType){        
        return authAxios.get('/courses', {
            headers: {
                Authorization: "Bearer " + localStorage.getItem('token')
            },
            params: {
                restaurantId: restaurantId,
                courseType: courseType
            }
        })
    }

    saveImage(courseId, image){        
        return authAxios.put('/courses/' + courseId + '/image', image, {
            headers: {
                Authorization: "Bearer " + localStorage.getItem('token'),
                'Content-Type': 'text/plain'
            },
        })
    }

    deleteCourse(courseId){
        return authAxios.delete('/courses/' + courseId, {
            headers: {
                Authorization: "Bearer " + localStorage.getItem('token')
            }
        })
    }

    getCourseTypes(){
        return authAxios.get('/courses/types', {
            headers: {
                Authorization: "Bearer " + localStorage.getItem('token')
            }
        })
    }
}

export default new CourseService()