	.file	"Mult.java"
	.section	.rodata
L16:	.string	" "
	.align 8
L17:	.string	"\n"
	.align 8
L12:	.string	""
	.align 8

	.align 8

	.section	.data
	.local v_0
	.comm v_0,8,8

	.section	.text
	.align 8
	.globl main
	.type	main,@function
main:
	jmp	m_0
	.text
	.align 8
m_0:
	pushq	%rbp
	movq	%rsp,%rbp
	subq	$0, %rsp
L19:
	movq $15,%rdi
	callq m_1
	movq %rax, %rdi 
	callq m_2
main_0:
	jmp L18
L18:
	leave
	ret


	.text
	.align 8
m_1:
	pushq	%rbp
	movq	%rsp,%rbp
	subq	$32, %rsp
L21:
	movq %r15,0(%rsp)
	movq %r14,8(%rsp)
	movq %r13,16(%rsp)
	movq %rdi, %r13 
	movq %r13, %rdi
	callq b_createArray_0
	movq %rax, %r14 
	movq $0,%r15
L0:
	cmpq %r13, %r15 
	jl L1
L2:
	movq %r14, %rax 
makeTable_0:
	movq 16(%rsp),%r13
	movq 8(%rsp),%r14
	movq 0(%rsp),%r15
	jmp L20
L1:
	movq %r13, %rdi
	callq b_createArray_0
	movq $8,%rdi
	movq %rdi, %rdi
	imulq %r15, %rdi
	movq %rdi, %rdi
	addq %r14, %rdi
	movq %rax,(%rdi) 
	movq $0,%rdi
L3:
	cmpq %r13, %rdi 
	jl L4
L5:
	movq %r15, %r15
	movq $1,%rdi
	addq %rdi, %r15
	jmp L0
L4:
	movq %r15, %rsi
	imulq %rdi, %rsi
	movq $8,%rdx
	movq %rdx, %rdx
	imulq %r15, %rdx
	movq %rdx, %rdx
	addq %r14, %rdx
	movq (%rdx), %rdx
	movq %rdx, %rdx
	movq %rdi, %r8
	movq $8,%rcx
	imulq %rcx, %r8
	addq %r8, %rdx
	movq %rsi,(%rdx) 
	movq %rdi, %rdi
	movq $1,%rsi
	addq %rsi, %rdi
	jmp L3
L20:
	leave
	ret


	.text
	.align 8
m_2:
	pushq	%rbp
	movq	%rsp,%rbp
	subq	$48, %rsp
L23:
	movq %r15,0(%rsp)
	movq %r14,8(%rsp)
	movq %r13,16(%rsp)
	movq %r12,24(%rsp)
	movq %rbx,32(%rsp)
	movq %rdi, %rbx 
	movq $0,%r12
L6:
	movq %rbx, %rsi
	movq $8,%rdi
	subq %rdi, %rsi
	movq (%rsi), %rdi
	cmpq %rdi, %r12 
	jl L7
L8:
printTable_0:
	movq 32(%rsp),%rbx
	movq 24(%rsp),%r12
	movq 16(%rsp),%r13
	movq 8(%rsp),%r14
	movq 0(%rsp),%r15
	jmp L22
L7:
	movq $0,%r13
L9:
	movq %rbx, %rsi
	movq $8,%rdi
	subq %rdi, %rsi
	movq (%rsi), %rdi
	cmpq %rdi, %r13 
	jl L10
L11:
	leaq L17(%rip),%rdi
	callq b_printString_0
	movq %r12, %r12
	movq $1,%rdi
	addq %rdi, %r12
	jmp L6
L10:
	movq $8,%rdi
	movq %rdi, %rdi
	imulq %r12, %rdi
	movq %rdi, %rdi
	addq %rbx, %rdi
	movq (%rdi), %rdi
	movq %rdi, %rdx
	movq %r13, %rsi
	movq $8,%rdi
	imulq %rdi, %rsi
	addq %rsi, %rdx
	movq (%rdx), %rdi
	callq b_intToString_0
	movq %rax, %rdi 
	leaq L12(%rip),%rsi
	callq b_stringConcatenate_0
	movq %rax, %r14 
	movq %r14, %rdi
	callq b_string_length_0
	movq %rax, %r15 
L13:
	movq $5,%rdi
	cmpq %rdi, %r15 
	jl L14
L15:
	movq %r14, %rdi
	callq b_printString_0
	movq %r13, %r13
	movq $1,%rdi
	addq %rdi, %r13
	jmp L9
L14:
	leaq L16(%rip),%rdi
	callq b_printString_0
	movq %r15, %r15
	movq $1,%rdi
	addq %rdi, %r15
	jmp L13
L22:
	leave
	ret


